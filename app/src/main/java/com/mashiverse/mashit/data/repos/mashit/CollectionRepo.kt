package com.mashiverse.mashit.data.repos.mashit

import com.mashiverse.mashit.data.local.db.entities.NftEntity
import com.mashiverse.mashit.data.models.mashi.mappers.toEntities
import com.mashiverse.mashit.data.models.mashup.MashupDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class CollectionRepo @Inject constructor(
    val alchemyRepo: AlchemyRepo,
    val nftRepo: NftRepo,
    val mashitRepo: MashitRepo,
) {
    val collectionFlow: Flow<List<NftEntity>> = nftRepo.ownedNftsFlow

    suspend fun updateOwnedData(wallet: String): Boolean {
        try {
            val newCollection = alchemyRepo.getCollection(wallet)
            val oldCollection = nftRepo.ownedNftsFlow.first()

            if (oldCollection.isEmpty()) {
                if (newCollection.isNotEmpty()) {
                    nftRepo.insertNfts(newCollection.toEntities())
                }
                return true
            }

            val newNames = newCollection.map { it.name }.toSet()
            val oldNames = oldCollection.map { it.name }.toSet()

            val toAdd = newCollection.filter { it.name !in oldNames }
            val toRemove = oldCollection.filter { it.name !in newNames }
            val toUpdate = newCollection.mapNotNull { new ->
                val old = oldCollection.find { it.name == new.name }
                if (old != null && new.owned != old.owned) old.copy(owned = new.owned) else null
            }

            if (toUpdate.isNotEmpty()) nftRepo.insertNfts(toUpdate)
            if (toAdd.isNotEmpty()) nftRepo.insertNfts(toAdd.toEntities())
            if (toRemove.isNotEmpty()) nftRepo.deleteNfts(toRemove)

            return true
        } catch (e: Exception) {
            Timber.tag("GG").d(e)
            return false
        }
    }

    suspend fun clearOwned() = nftRepo.clearOwned()

    suspend fun getMashup(wallet: String): MashupDetails {
        return mashitRepo.getMashup(wallet)
    }
}