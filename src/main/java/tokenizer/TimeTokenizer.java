package tokenizer;

import bean.TimeItem;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

/**
 * Created by jet on 2017/7/27.
 */
public class TimeTokenizer extends Tokenizer {
    
    private CharTermAttribute termAttr;
    private OffsetAttribute offsetAttr;

    private char[] buf = new char[4096];
    private int index = 0;
    private int finalOffset = 0;
    private StringBuilder sb=null;
    private ArrayList<TimeItem> timeItems=null;

    public TimeTokenizer(Reader input) {
        super(input);
        termAttr = this.addAttribute(CharTermAttribute.class);
        offsetAttr = this.addAttribute(OffsetAttribute.class);
    }

    @Override
    public final boolean incrementToken() throws IOException {
        clearAttributes();
       if(sb==null){
           sb = new StringBuilder();
       }
        while (input.read(buf) > 0) {
            sb.append(buf);
        }

        if (!"".equals(sb.toString())) {
            timeItems = JSON.parseObject(sb.toString(), new TypeReference<ArrayList<TimeItem>>(){});
        }

        if (timeItems != null && timeItems.size() > index) {
            TimeItem item = timeItems.get(index);
            termAttr.append(item.getText());
            offsetAttr.setOffset(item.getBegin(), item.getEnd());
            finalOffset = item.getEnd();
            index ++;
            return true;
        }

        return false;
    }

    public final void end() throws IOException {
        super.end();
        this.offsetAttr.setOffset(this.finalOffset, this.finalOffset);
    }

    public void reset() throws IOException {
        super.reset();
        this.index = 0;
        this.finalOffset = 0;
        this.buf=new char[4096];
       if(this.timeItems!=null){
           this.timeItems.clear();
       }
           if(this.sb!=null){
           this.sb.delete(0,sb.length());
       }
    }
}
