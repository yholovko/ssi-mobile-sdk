package com.dxc.ssi.agent.didcomm.model.verify

import com.dxc.ssi.agent.utils.indy.IndySerializationUtils.jsonProcessor
import com.dxc.ssi.agent.wallet.indy.model.verify.IndyPresentation
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlin.test.Test
import kotlin.test.assertEquals

class IndyPresentationTest {

    @Test
    fun testDeserialization() {

        val presentationJson =
            "{\"proof\":{\"proofs\":[{\"primary_proof\":{\"eq_proof\":{\"revealed_attrs\":{\"age\":\"18\",\"name\":\"62816810226936654797779705000772968058283780124309077049681734835796332704413\",\"vaccination\":\"62633904145874432068825250338234575811152650764116402243604522180417582636349\"},\"a_prime\":\"110663892691622277527945504773728004716182335154955133748281727092149105769763980445451969776746877653609006697127107547898342578457716280402842935891644690838995714746312353010034193135775034207939849712537699519560154477619126180982203857244230721786415249597191379717786786498647945051562839629934420051756306835494036967300903699788361023231609228860914110466029618039632598806993425144845316739732525135163490337232641215101700862956619904561808322474869013588638911870314502346486400670448269494098534756188269109730264454028294841375743507762956660203748071649107338544772079956167483168787007421457675470295233\",\"e\":\"15498744163820419278021507958747666807897669776011645126730356886692167046738361389234037151467580540041955439769369859186701509138025719\",\"v\":\"69622027507131149576657325382454247095053851079191323849925041099068267764497178947374611255344058212414727979161402070147787316446889227739079088577240504059946492672762016326725638602518484723062171100189126642639731175255890645440452610040992670657915371691852598626634446675563950675853528266266067647210006863386508094261549247197452438037843068600157506643935340666628806785640162872825096632899347338555246946816582056030190080485765684547995702859465893128787258718034956471799918676822026370474466662639783870756492856962585641145824895285169169380032200094949266380643518279758583030830074488975952015721576004613849005564019483380769152458183160834928192079617206524443289420305099972469688457483790945736379732416519260804853611909559127689194108867498688556000422397672870586499117211409461070784574359647549538029845292073854613702968057175960304915799217559427933659233419389511220844410269731730160295893\",\"m\":{\"master_secret\":\"4399995947357627168183021106498555393905404913046375829011737320185637074172695181248167900495338116628031724192015572485437442804175960978938325169149620469496925121692233935698\"},\"m2\":\"14847099505591243244006304985833290801365649563866488875815336556069928083849049460062077697534559804319763107400127961680064521749546985534526741246232396925950465964134299218277\"},\"ge_proofs\":[]},\"non_revoc_proof\":null}],\"aggregated_proof\":{\"c_hash\":\"43013047684373215833841158612564701194188163757184721526369417047927934749792\",\"c_list\":[[3,108,160,123,222,80,124,106,169,213,171,49,47,252,106,52,158,135,108,119,175,108,66,61,61,153,167,134,87,100,207,104,250,214,148,222,18,173,75,75,77,254,172,81,117,211,152,142,13,238,75,230,55,114,66,196,242,4,202,99,121,70,39,221,111,81,186,69,147,96,108,177,125,182,171,99,239,212,135,218,161,162,121,67,114,5,224,194,24,124,34,211,50,27,205,9,179,205,54,174,6,83,103,240,244,167,27,242,106,85,40,54,145,119,95,235,170,192,128,75,16,54,252,39,175,22,13,146,37,222,32,52,166,17,102,3,109,232,122,160,33,90,96,209,10,220,94,245,232,140,168,1,187,237,182,25,252,18,84,68,30,14,205,172,174,246,98,105,198,96,99,140,184,105,124,152,246,185,168,104,168,170,149,129,233,238,18,63,169,117,159,0,165,72,85,70,191,250,58,214,176,20,212,213,79,216,109,115,192,186,106,109,41,184,95,142,68,117,118,161,111,112,212,134,214,129,225,224,184,69,234,223,176,144,233,245,36,98,81,168,54,83,61,11,32,147,173,139,182,134,44,211,173,243,224,208,193]]}},\"requested_proof\":{\"revealed_attrs\":{\"vaccination-requirement\":{\"sub_proof_index\":0,\"raw\":\"yes\",\"encoded\":\"62633904145874432068825250338234575811152650764116402243604522180417582636349\"},\"age-requirement\":{\"sub_proof_index\":0,\"raw\":\"18\",\"encoded\":\"18\"},\"first-name-requirement\":{\"sub_proof_index\":0,\"raw\":\"Alice Smith\",\"encoded\":\"62816810226936654797779705000772968058283780124309077049681734835796332704413\"}},\"self_attested_attrs\":{},\"unrevealed_attrs\":{},\"predicates\":{}},\"identifiers\":[{\"schema_id\":\"AYccqwTejE782bEdDmvNCL:2:vaccination-schema:1.0\",\"cred_def_id\":\"AYccqwTejE782bEdDmvNCL:3:CL:48:default\",\"rev_reg_id\":null,\"timestamp\":null}]}"

        println("raw presentationRequestJson: $presentationJson")


        val presentation = jsonProcessor.decodeFromString<IndyPresentation>(presentationJson)

        println("parsed presentation : $presentation")

        val encodeToString = jsonProcessor.encodeToString(presentation)

        println("serialized back presentation : $presentation")

        assertEquals(presentationJson, encodeToString)
    }


}