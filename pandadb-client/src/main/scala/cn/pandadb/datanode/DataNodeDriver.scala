package cn.pandadb.datanode

import cn.pandadb.driver.result.InternalRecords
import cn.pandadb.driver.values.{Node, Relationship}
import cn.pandadb.driver.util.PandaReplyMsg
import net.neoremind.kraps.rpc.netty.HippoEndpointRef
//import org.neo4j.graphdb.Direction

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class DataNodeDriver {

  def runCypher(cypher: String, endpointRef: HippoEndpointRef, duration: Duration): InternalRecords = {
    val res = Await.result(endpointRef.askWithBuffer[InternalRecords](RunCypher(cypher)), duration)
    res
  }

  def sayHello(msg: String, endpointRef: HippoEndpointRef, duration: Duration): PandaReplyMsg.Value = {
    val res = Await.result(endpointRef.askWithBuffer[PandaReplyMsg.Value](SayHello(msg)), duration)
    res
  }

  def createNode(labels: Array[String], properties: Map[String, Any], endpointRef: HippoEndpointRef, duration: Duration): Node = {
    val res = Await.result(endpointRef.askWithBuffer[Node](CreateNode(labels, properties)), duration)
    res
  }

  def addNodeLabel(id: Long, label: String, endpointRef: HippoEndpointRef, duration: Duration): PandaReplyMsg.Value = {
    val res = Await.result(endpointRef.askWithBuffer[PandaReplyMsg.Value](AddNodeLabel(id, label)), duration)
    res
  }

  def getNodeById(id: Long, endpointRef: HippoEndpointRef, duration: Duration): Node = {
    val node = Await.result(endpointRef.askWithBuffer[Node](GetNodeById(id)), duration)
    node
  }

  def getNodesByProperty(label: String, propertiesMap: Map[String, Object], endpointRef: HippoEndpointRef, duration: Duration): ArrayBuffer[Node] = {
    implicit def any2Object(x: Map[String, Any]): Map[String, Object] = x.asInstanceOf[Map[String, Object]]

    val res = Await.result(endpointRef.askWithBuffer[ArrayBuffer[Node]](GetNodesByProperty(label, propertiesMap)), duration)
    res
  }

  def getNodesByLabel(label: String, endpointRef: HippoEndpointRef, duration: Duration): ArrayBuffer[Node] = {
    val res = Await.result(endpointRef.askWithBuffer[ArrayBuffer[Node]](GetNodesByLabel(label)), duration)
    res
  }

  def updateNodeProperty(id: Long, propertiesMap: Map[String, Any], endpointRef: HippoEndpointRef, duration: Duration): PandaReplyMsg.Value = {
    val res = Await.result(endpointRef.askWithBuffer[PandaReplyMsg.Value](UpdateNodeProperty(id, propertiesMap)), duration)
    res
  }

  def updateNodeLabel(id: Long, toDeleteLabel: String, newLabel: String, endpointRef: HippoEndpointRef, duration: Duration): PandaReplyMsg.Value = {
    val res = Await.result(endpointRef.askWithBuffer[PandaReplyMsg.Value](UpdateNodeLabel(id, toDeleteLabel, newLabel)), duration)
    res
  }

  def deleteNode(id: Long, endpointRef: HippoEndpointRef, duration: Duration): PandaReplyMsg.Value = {
    val res = Await.result(endpointRef.askWithBuffer[PandaReplyMsg.Value](DeleteNode(id)), duration)
    res
  }

  def removeProperty(id: Long, property: String, endpointRef: HippoEndpointRef, duration: Duration): PandaReplyMsg.Value = {
    val res = Await.result(endpointRef.askWithBuffer[PandaReplyMsg.Value](RemoveProperty(id, property)), duration)
    res
  }
//
//  def createNodeRelationship(id1: Long, id2: Long, relationship: String, direction: Direction, endpointRef: HippoEndpointRef, duration: Duration): PandaReplyMsg.Value = {
//    val res = Await.result(endpointRef.askWithBuffer[PandaReplyMsg.Value](CreateNodeRelationship(id1, id2, relationship, Direction.OUTGOING)), duration)
//    res
//  }

  def getNodeRelationships(id: Long, endpointRef: HippoEndpointRef, duration: Duration): ArrayBuffer[Relationship] = {
    val res = Await.result(endpointRef.askWithBuffer[ArrayBuffer[Relationship]](GetNodeRelationships(id)), duration)
    res
  }
//
//  def deleteNodeRelationship(id: Long, relationship: String, direction: Direction, endpointRef: HippoEndpointRef, duration: Duration): PandaReplyMsg.Value = {
//    val res = Await.result(endpointRef.askWithBuffer[PandaReplyMsg.Value](DeleteNodeRelationship(id, relationship, Direction.OUTGOING)), duration)
//    res
//  }

  def getAllDBNodes(chunkSize: Int, endpointRef: HippoEndpointRef, duration: Duration): Stream[Node] = {
    val res = endpointRef.getChunkedStream[Node](GetAllDBNodes(chunkSize), duration)
    res
  }

  def getAllDBRelationships(chunkSize: Int, endpointRef: HippoEndpointRef, duration: Duration): Stream[Relationship] = {
    val res = endpointRef.getChunkedStream[Relationship](GetAllDBRelationships(chunkSize), duration)
    res
  }
}
