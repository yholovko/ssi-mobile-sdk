package com.dxc.ssi.agent.didcomm.processor.didexchange

import com.dxc.ssi.agent.api.callbacks.didexchange.ConnectionInitiatorController
import com.dxc.ssi.agent.api.callbacks.didexchange.ConnectionResponderController
import com.dxc.ssi.agent.api.pluggable.Transport
import com.dxc.ssi.agent.api.pluggable.wallet.WalletConnector
import com.dxc.ssi.agent.didcomm.actions.didexchange.impl.ReceiveConnectionResponseAction
import com.dxc.ssi.agent.didcomm.actions.didexchange.impl.ReceiveInvitationAction
import com.dxc.ssi.agent.didcomm.model.didexchange.ConnectionResponse
import com.dxc.ssi.agent.didcomm.processor.trustping.TrustPingProcessor
import com.dxc.ssi.agent.didcomm.states.State
import com.dxc.ssi.agent.didcomm.states.StateMachine
import com.dxc.ssi.agent.didcomm.states.didexchange.DidExchangeStateMachine
import com.dxc.ssi.agent.model.Connection
import com.dxc.ssi.agent.model.messages.BasicMessageWithTypeOnly
import com.dxc.ssi.agent.model.messages.Message
import com.dxc.ssi.agent.model.messages.MessageContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

//TODO: for now this class won;t be part of abstraction, once it is implemented see if it is posible to generalize it with MessageProcessor and AbstractMessageProcessor
//TODO: rename DidExchange to Connection everywhere? Since Did exchange spec is not yet in active status
class DidExchangeProcessorImpl(
    private val walletConnector: WalletConnector,
    private val transport: Transport,
    private val trustPingProcessor: TrustPingProcessor,
    val connectionInitiatorController: ConnectionInitiatorController?,
    val connectionResponderController: ConnectionResponderController?
) : DidExchangeProcessor {

    private val stateMachine = DidExchangeStateMachine()

    //TODO: reuse kotlin possibility to override states to make abstract property instead of abstract method if possible
    fun getStateMachine(): StateMachine {
        return stateMachine
    }

    fun getCurrentState(): State {
        // walletConnector.walletHolder.getConnectionRecordById()
        TODO("Not implemented")
    }

    override fun initiateConnectionByInvitation(invitation: String): Connection {

        //TODO: think how to avoid NPE here
        val receiveInvitationAction =
            ReceiveInvitationAction(walletConnector, transport, connectionInitiatorController!!, invitation)
        return receiveInvitationAction.perform().connection!!
    }

    //TODO: see if it is possible to generalize this function in order to go back to AbstractProcessor
    override fun processMessage(messageContext: MessageContext) {

        println("Started processing message $messageContext")

        //1. Determine message type and parse message


        when (getMessageType(messageContext.receivedUnpackedMessage.message)) {
            DidExchangeMessageType.INVITATION -> TODO("Not implemented")
            DidExchangeMessageType.CONNECTION_REQUEST -> TODO("Not implemented")
            DidExchangeMessageType.CONNECTION_RESPONSE -> {
                val connectionResponseMesage =
                    Json { ignoreUnknownKeys = true }.decodeFromString<ConnectionResponse>(messageContext.receivedUnpackedMessage.message)


                val ourConnectionId = connectionResponseMesage.thread.thid

                //TODO: think about moving wallet operation out of this abstraction layer. Think about moving it to actions layer
                //TODO: looks like this is not needed any more since we extract connection nin MessageListener and provide it in message context
                val ourConnection = walletConnector.walletHolder.getConnectionRecordById(ourConnectionId)

                println("Existing connection record $ourConnection")

                //TODO: move the decision below to some abstraction layer? StateMahine?
                when (ourConnection!!.state) {
                    //TODO: Create some enum for possible states
                    "RequestSent" -> {
                        ReceiveConnectionResponseAction(
                            walletConnector,
                            transport,
                            connectionInitiatorController!!,
                            connectionResponseMesage,
                            messageContext,
                            ourConnection
                        ).perform()

                        //TODO: decide on what abstraction layer trust ping should be sent! Should not it be sent to inbound message to start from the beginning?
                        trustPingProcessor.sendTrustPingOverConnection(ourConnection)
                    }

                    else -> TODO("Not implemented") // process different possibilities here according to state diagram

                }

            }

        }

    }

    enum class DidExchangeMessageType {
        //TODO:  add ProblemReport/error message
        INVITATION,
        CONNECTION_REQUEST,
        CONNECTION_RESPONSE
    }

    private fun getMessageType(message: String): DidExchangeMessageType {

        val typeAttribute =
            Json { ignoreUnknownKeys = true }.decodeFromString<BasicMessageWithTypeOnly>(message).type

        //TODO: change to regexp to allow more strict matching
        val messageType = when {
            typeAttribute.contains("connections/1.0/invitation") -> DidExchangeMessageType.INVITATION
            typeAttribute.contains("connections/1.0/request") -> DidExchangeMessageType.CONNECTION_REQUEST
            typeAttribute.contains("connections/1.0/response") -> DidExchangeMessageType.CONNECTION_RESPONSE
            else -> throw IllegalArgumentException("Unknown message type: $typeAttribute")
        }

        println("Determined message type: $messageType")

        return messageType
    }

}