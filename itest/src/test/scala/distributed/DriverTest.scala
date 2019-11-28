package distributed

import java.io.{File, FileInputStream}
import java.util.Properties

import org.junit.{Assert, Test}
import org.neo4j.driver.{AuthTokens, GraphDatabase}

/**
  * Created by bluejoe on 2019/11/21.
  */
class DriverTest {


  val configFile = new File("./testdata/gnode0.conf")
  val props = new Properties()
  props.load(new FileInputStream(configFile))
  val pandaString = s"panda://" + props.getProperty("zkServerAddress") + s"/db"

  //test CRUD
  @Test
  def test1() {
    val driver = GraphDatabase.driver(pandaString,
      AuthTokens.basic("", ""));
    val session = driver.session();
    var results1 = session.run("create (n:person{name:'bluejoe'})");
    val results = session.run("match (n) return n.name");
    val result = results.next();
    Assert.assertEquals("bluejoe", result.get("n.name").asString());
    val results2 = session.run("match (n:person) delete n");
    session.close();
    driver.close();
  }


  //test transaction
  @Test
  def test2() {
    val driver = GraphDatabase.driver(pandaString,
      AuthTokens.basic("", ""));
    val session = driver.session();
    val transaction = session.beginTransaction()
    var results1 = transaction.run("create (n:person{name:'bluejoe'})");

    results1 = transaction.run("create (n:people{name:'lin'})");
    val results = transaction.run("match (n) return n.name");

    val result = results.next();
    Assert.assertEquals("bluejoe", result.get("n.name").asString());
    val results2 = transaction.run("match (n) delete n");
    transaction.success()
    transaction.close()
    session.close();
    driver.close();
  }

//  //test
//  @Test
//  def test3() {
//    val driver = GraphDatabase.driver("bolt://10.0.86.179:7687",
//      AuthTokens.basic("neo4j", "123456"));
//    val session = driver.session();
//    val transaction = session.beginTransaction()
//    //val transaction2 = session.beginTransaction()
//    //session.close()
//    val results1 = transaction.run("create (n:person{name:'bluejoe'})");
//
//    val results3 = transaction.run("create (n:people{name:'lin'})");
//
//    val results = transaction.run("match (n:person) return n.name");
//
//    val result = results.next();
//    Assert.assertEquals("bluejoe", result.get("n.name").asString());
//    val results2 = transaction.run("match (n) delete n");
//    transaction.success()
//    transaction.close()
//    session.close();
//    driver.close();
//  }


}
