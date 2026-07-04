package com.mashiverse.mashit.data.repos.mashit

import com.mashiverse.mashit.data.models.mashi.Nft
import com.mashiverse.mashit.data.models.mashi.NftDetails
import com.mashiverse.mashit.data.models.mashi.Owned
import com.mashiverse.mashit.data.models.mashi.Trait
import com.mashiverse.mashit.data.models.mashi.TraitType
import com.mashiverse.mashit.data.models.mashi.mappers.toTraits
import com.mashiverse.mashit.data.remote.apis.AlchemyApi
import com.mashiverse.mashit.data.remote.apis.IpfsApi
import com.mashiverse.mashit.utils.ALCHEMY_KEY
import com.mashiverse.mashit.utils.MASHI_ADDRESS
import com.mashiverse.mashit.utils.helpers.nft.parseName
import com.mashiverse.mashit.utils.helpers.nft.fromIpfsScheme
import com.mashiverse.mashit.utils.helpers.nft.toFilebaseUri
import com.mashiverse.mashit.utils.helpers.nft.toIpfsPartialUri
import timber.log.Timber
import javax.inject.Inject

class AlchemyRepo @Inject constructor(
    private val alchemyApi: AlchemyApi,
    private val ipfsApi: IpfsApi
) {

    suspend fun getCollection(wallet: String): List<Nft> {
        val nfts: MutableList<Nft> = mutableListOf()
        var key: String? = null

        try {
            while (true) {
                val data = alchemyApi.getNFTsForOwner(
                    apiKey = ALCHEMY_KEY,
                    withMetadata = true,
                    owner = wallet,
                    contractAddress = MASHI_ADDRESS,
                    pageKey = key
                )

                val ownedNfts = data.ownedNfts
                if (ownedNfts.isEmpty()) return emptyList()

                ownedNfts.forEach { nft ->
                    val metadata = nft.raw.metadata
                    var details = NftDetails("", "", -1)
                    val description = metadata.description
                    val tokenUri = nft.tokenUri.toFilebaseUri().toIpfsPartialUri()

                    val assets = metadata.assets
                    val (compositeUrl: String, traits: List<Trait>) = try {
                        val url = metadata.image.fromIpfsScheme()
                        val traits =  assets.toTraits()
                        details = parseName(metadata.name)

                        url to traits
                    } catch (_: Exception) {
                        val ipfsMetadata = ipfsApi.getMetadataByIpfsUri(tokenUri)

                        val url = ipfsMetadata.image.fromIpfsScheme()
                        val traits = ipfsMetadata.assets.map { asset ->
                            Trait(
                                url = asset.uri.fromIpfsScheme(),
                                type = TraitType.valueOf(asset.label.uppercase())
                            )
                        }
                        details = parseName(ipfsMetadata.name)

                        url to traits
                    } finally {
                        if (details.mint == -1) {
                            val ipfsMetadata = ipfsApi.getMetadataByIpfsUri(tokenUri)
                            details = parseName(ipfsMetadata.name)
                        }
                    }

                    val currentOwned = Owned(
                        mint = details.mint,
                        timestamp = nft.timeLastUpdated
                    )

                    val tempNft = nfts.firstOrNull { nft -> nft.name == details.name }

                    tempNft?.let {
                        val owned = tempNft.owned?.toMutableList() ?: mutableListOf()
                        owned.add(currentOwned)

                        val updatedNft = tempNft.copy(owned = owned)

                        nfts.remove(tempNft)
                        nfts.add(updatedNft)

                        return@forEach
                    }

                    nfts.add(
                        Nft(
                            name = details.name,
                            description = description,
                            compositeUrl = compositeUrl,
                            traits = traits,
                            author = details.authorName,
                            owned = listOf(currentOwned)
                        )
                    )
                }

                key = data.pageKey
                if (key == null) return nfts
            }
        } catch (e: Exception) {
            return emptyList()
        }
    }
}