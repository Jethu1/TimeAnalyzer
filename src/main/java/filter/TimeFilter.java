package filter;

import bean.TimeItem;
import org.apache.lucene.index.*;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.Filter;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.FixedBitSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jet on 2017/7/27.
 */
public class TimeFilter extends Filter {
    private String field;

    private String value;

    private int begin;

    private int end;

    private Map<Integer, List<TimeItem>> hitMap=new HashMap<Integer, List<TimeItem>>();//返回过滤后的结果；

    public TimeFilter(String field, String value, int begin, int end) {
        this.field = field;
        this.value = value;
        this.begin = begin;
        this.end = end;

    }

    public Map<Integer, List<TimeItem>> getHitMap() {
        return hitMap;
    }

    public DocIdSet getDocIdSet(AtomicReaderContext atomicReaderContext, Bits bits) throws IOException {
        FixedBitSet result = new FixedBitSet(atomicReaderContext.reader().maxDoc());
        Terms terms = atomicReaderContext.reader().terms(field);
        if(terms==null)
            return  null;

        TermsEnum termsEnum = terms.iterator(null);
        if(termsEnum==null)
            return  null;

        if(termsEnum.seekExact(new BytesRef(value))){

            DocsAndPositionsEnum positionsEnum = termsEnum.docsAndPositions(bits,null, DocsEnum.FLAG_FREQS);
            while(positionsEnum.nextDoc()!= DocIdSetIterator.NO_MORE_DOCS){
                if(positionsEnum.freq()<1)//没有信息
                    continue;

                List<TimeItem> timeItems = new ArrayList<TimeItem>();
                for (int i = 0; i < positionsEnum.freq() ; i++) {
                    positionsEnum.nextPosition();
                    TimeItem item = new TimeItem();
                    if(positionsEnum.startOffset()>=begin&&positionsEnum.endOffset()<=end){
                        item.setBegin(positionsEnum.startOffset());
                        item.setEnd(positionsEnum.endOffset());
                        item.setText(value);
                        timeItems.add(item);
                    }
                }
                if(timeItems.size()>0){
                    result.set(positionsEnum.docID());
                    hitMap.put(positionsEnum.docID(),timeItems);
                }
            }
        }
        return result;
    }


}
