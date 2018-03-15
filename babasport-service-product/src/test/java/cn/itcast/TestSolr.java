package cn.itcast;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application-context.xml" })
public class TestSolr {
 @Autowired
 private SolrServer solrServer;
 @Test
 public void testSolr() throws SolrServerException, IOException{
	 SolrInputDocument doc=new SolrInputDocument();
	 doc.addField("id", 3);
	 doc.addField("name", "fanbinbin");
	 solrServer.add(doc);
	 
	 //注意提交
	 solrServer.commit();
	 
 }
	
}
