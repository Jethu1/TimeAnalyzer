import bean.TimeField;
import bean.TimeItem;
import filter.TimeFilter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import tokenizer.TimeAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class IndexSearchTest {

    @Test
    public void testIndex() throws IOException {

        Directory directory = FSDirectory.open(new File("index"));
        Analyzer timeAnalyzer = new TimeAnalyzer(Version.LUCENE_46);
        IndexWriterConfig writerConfig = new IndexWriterConfig(Version.LUCENE_46, timeAnalyzer);
        writerConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter indexWriter = new IndexWriter(directory, writerConfig);

        String text0 = "[" +
                "{\"begin\":100,\"end\":400,\"text\":\"你好\"}," +
                "{\"begin\":400,\"end\":600,\"text\":\"八十五号\"}," +
                "{\"begin\":600,\"end\":830,\"text\":\"为您\"}," +
                "{\"begin\":900,\"end\":1050,\"text\":\"服务\"}," +
                "{\"begin\":1050,\"end\":1400,\"text\":\"你好\"}," +
                "{\"begin\":1400,\"end\":1600,\"text\":\"我\"}," +
                "{\"begin\":1600,\"end\":1830,\"text\":\"这个\"}," +
                "{\"begin\":1830,\"end\":2030,\"text\":\"修车\"}," +
                "{\"begin\":2030,\"end\":2300,\"text\":\"这里\"}," +
                "{\"begin\":2300,\"end\":2530,\"text\":\"附近有\"}," +
                "{\"begin\":2530,\"end\":2830,\"text\":\"四s店\"}," +
                "{\"begin\":2830,\"end\":3330,\"text\":\"可以修车\"}," +
                "{\"begin\":3330,\"end\":3800,\"text\":\"西二旗\"}," +
                "{\"begin\":3800,\"end\":4830,\"text\":\"这边\"}," +
                "{\"begin\":4830,\"end\":5530,\"text\":\"有修车店吗\"}," +
                "{\"begin\":5530,\"end\":6600,\"text\":\"有的\"}," +
                "{\"begin\":6600,\"end\":6830,\"text\":\"但是\"}," +
                "{\"begin\":6830,\"end\":8030,\"text\":\"你需要\"}," +
                "{\"begin\":8030,\"end\":9030,\"text\":\"修车\"}," +
                "{\"begin\":9030,\"end\":10030,\"text\":\"定损\"}," +
                "{\"begin\":10030,\"end\":10830,\"text\":\"谢谢\"}," +
                "{\"begin\":10830,\"end\":13000,\"text\":\"不客气\"}]";

        String text1 = "[" +
                "{\"begin\":100,\"end\":400,\"text\":\"你好\"}," +
                "{\"begin\":400,\"end\":600,\"text\":\"客户\"}," +
                "{\"begin\":600,\"end\":830,\"text\":\"您需要\"}," +
                "{\"begin\":900,\"end\":1050,\"text\":\"服务吗\"}," +
                "{\"begin\":1050,\"end\":1400,\"text\":\"你好\"}," +
                "{\"begin\":1400,\"end\":1600,\"text\":\"我\"}," +
                "{\"begin\":1600,\"end\":1830,\"text\":\"这个\"}," +
                "{\"begin\":1830,\"end\":2030,\"text\":\"大车\"}," +
                "{\"begin\":2030,\"end\":2300,\"text\":\"这里\"}," +
                "{\"begin\":2300,\"end\":2530,\"text\":\"附近有\"}," +
                "{\"begin\":2530,\"end\":2830,\"text\":\"四s店\"}," +
                "{\"begin\":2830,\"end\":3330,\"text\":\"车\"}," +
                "{\"begin\":3330,\"end\":3800,\"text\":\"西二旗\"}," +
                "{\"begin\":3800,\"end\":4830,\"text\":\"这边\"}," +
                "{\"begin\":4830,\"end\":5530,\"text\":\"车\"}," +
                "{\"begin\":5530,\"end\":6600,\"text\":\"有的\"}," +
                "{\"begin\":6600,\"end\":6830,\"text\":\"但是\"}," +
                "{\"begin\":6830,\"end\":8030,\"text\":\"你需要\"}," +
                "{\"begin\":8030,\"end\":9030,\"text\":\"修车\"}," +
                "{\"begin\":9030,\"end\":10030,\"text\":\"定损\"}," +
                "{\"begin\":10030,\"end\":10830,\"text\":\"谢谢\"}," +
                "{\"begin\":10830,\"end\":13000,\"text\":\"不客气\"}]";

        String text2 = "[" +
                "{\"begin\":13000,\"end\":14000,\"text\":\"你\"}," +
                "{\"begin\":14000,\"end\":16600,\"text\":\"好\"}," +
                "{\"begin\":16600,\"end\":17830,\"text\":\"您\"}," +
                "{\"begin\":17900,\"end\":21050,\"text\":\"务\"}," +
                "{\"begin\":21050,\"end\":21400,\"text\":\"你好\"}," +
                "{\"begin\":21400,\"end\":21600,\"text\":\"我\"}," +
                "{\"begin\":21600,\"end\":21830,\"text\":\"这个\"}," +
                "{\"begin\":21830,\"end\":22030,\"text\":\"大车\"}," +
                "{\"begin\":22030,\"end\":22300,\"text\":\"这里\"}," +
                "{\"begin\":22300,\"end\":22530,\"text\":\"附近有\"}," +
                "{\"begin\":22530,\"end\":22830,\"text\":\"四s店\"}," +
                "{\"begin\":22830,\"end\":23330,\"text\":\"车\"}," +
                "{\"begin\":23330,\"end\":23800,\"text\":\"西二旗\"}," +
                "{\"begin\":23800,\"end\":24830,\"text\":\"这边\"}," +
                "{\"begin\":24830,\"end\":25530,\"text\":\"车\"}," +
                "{\"begin\":25530,\"end\":26600,\"text\":\"有的\"}," +
                "{\"begin\":26600,\"end\":26830,\"text\":\"但是\"}," +
                "{\"begin\":26830,\"end\":28030,\"text\":\"你需要\"}," +
                "{\"begin\":28030,\"end\":29030,\"text\":\"修车\"}," +
                "{\"begin\":29030,\"end\":31030,\"text\":\"定损\"}," +
                "{\"begin\":31030,\"end\":32830,\"text\":\"谢谢\"}," +
                "{\"begin\":32830,\"end\":33000,\"text\":\"不客气\"}]";

        Document doc = new Document();
        Document doc1 = new Document();
        Document doc2 = new Document();
        doc.add(new TimeField("Contents", text0, Field.Store.YES));
        doc1.add(new TimeField("Contents", text1, Field.Store.YES));
        doc2.add(new TimeField("Contents", text2, Field.Store.YES));
        indexWriter.addDocument(doc);
        indexWriter.addDocument(doc1);
        indexWriter.addDocument(doc2);
        indexWriter.close();
    }

    @Test
    public void testSearch() {
        DirectoryReader directoryReader = null;
        //设置索引目录
        try {
            Directory directory = FSDirectory.open(new File("index"));
            //创建IndexReader
            directoryReader = DirectoryReader.open(directory);
            //根据IndexReader创建IndexSearch
            IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
            //创建搜索的Query
            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
            //创建parser确定要搜索文件的内容，第一个参数为搜索的域
//            QueryParser queryParser = new QueryParser(Version.LUCENE_46,"Contents", analyzer);
            //创建Query表示搜索域包含指定关键词的文档
//            Query query = queryParser.parse("车");
//            TopDocs topDocs = indexSearcher.search(termQuery, 10);
            TermQuery termQuery = new TermQuery(new Term("Contents","修车"));
            TimeFilter filter = new TimeFilter("Contents","修车",100,20000);
            ConstantScoreQuery query = new ConstantScoreQuery(termQuery);
            //根据searcher搜索并且返回TopDocs
            TopDocs topDocs = indexSearcher.search(query, filter,10);

            System.out.println("查找到的文档总共有: " + topDocs.totalHits);
            Iterator<Map.Entry<Integer, List<TimeItem>>> it = filter.getHitMap().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Integer, List<TimeItem>> entry = it.next();
                System.out.println("文档"+((int)entry.getKey()+1)+"的具体时间位置： "  + entry.getValue());
            }

            //根据TopDocs获取ScoreDoc对象
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document doc = indexSearcher.doc(scoreDoc.doc);// 根据文档的标识获取文档
                String time = doc.get("Contents");
                System.out.println("整篇文档的分数:"+scoreDoc.score+" 内容： "+time);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (directoryReader != null)
                    directoryReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    public void spanNearSearch() {
        DirectoryReader directoryReader = null;
        //设置索引目录
        try {
            Directory directory = FSDirectory.open(new File("index"));
            //创建IndexReader
            directoryReader = DirectoryReader.open(directory);
            //根据IndexReader创建IndexSearch
            IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
            //创建搜索的Query
            SpanTermQuery[] queries = new SpanTermQuery[2];
            queries[1] = new SpanTermQuery(new Term("Contents","为您"));
            queries[0] = new SpanTermQuery(new Term("Contents","你好"));
            SpanNearQuery spanNearQuery = new SpanNearQuery(queries,1,false);

//          TopDocs topDocs = indexSearcher.search(termQuery, 10);
            TermQuery termQuery = new TermQuery(new Term("Contents","修车"));
            TimeFilter filter = new TimeFilter("Contents","为您",100,20000);

            ConstantScoreQuery query = new ConstantScoreQuery(spanNearQuery);
            //根据searcher搜索并且返回TopDocs
            TopDocs topDocs = indexSearcher.search(query, 10);

            System.out.println("查找到的文档总共有: " + topDocs.totalHits);
            Iterator<Map.Entry<Integer, List<TimeItem>>> it = filter.getHitMap().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Integer, List<TimeItem>> entry = it.next();
                System.out.println("文档"+((int)entry.getKey()+1)+"的具体时间位置： "  + entry.getValue());
            }

            //根据TopDocs获取ScoreDoc对象
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document doc = indexSearcher.doc(scoreDoc.doc);// 根据文档的标识获取文档
                String time = doc.get("Contents");
                System.out.println("整篇文档的分数:"+scoreDoc.score+" 内容： "+time);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (directoryReader != null)
                    directoryReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

