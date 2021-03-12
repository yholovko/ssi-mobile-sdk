package com.dxc.ssi.agent.wallet.indy

import com.dxc.ssi.agent.api.pluggable.wallet.Issuer
import com.dxc.ssi.agent.api.pluggable.wallet.WalletHolder
import com.dxc.ssi.agent.model.Connection
import com.dxc.ssi.agent.model.IdentityDetails
import com.dxc.ssi.agent.model.messages.Message

actual class IndyIssuer actual constructor(walletHolder: WalletHolder) : IndyWalletHolder(), Issuer {
    actual override  fun sign(data: String): String {
        TODO("Not yet implemented")
    }

}