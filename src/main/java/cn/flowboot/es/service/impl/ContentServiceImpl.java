package cn.flowboot.es.service.impl;

import cn.flowboot.es.doc.Good;
import cn.flowboot.es.service.ContentService;
import cn.flowboot.es.service.ElasticSearchService;
import cn.flowboot.es.utils.SearchJdGoodUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <h1></h1>
 *
 * @version 1.0
 * @author: Vincent Vic
 * @since: 2021/08/24
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ContentServiceImpl implements ContentService {

    private final ElasticSearchService<Good> elasticSearchService;

    private final SearchJdGoodUtils searchJdGoodUtils;


    @Async("getAsyncExecutor")
    @Override
    public void captureJdDataAsync(String keyword) {
        try {
            //"spring", "html","java", "c"
            List<String> keys = Arrays.asList("python", "bigdata", "hadoop", "mac", "iphone", "mi", "it", "css", "html", "vue", "excel","huawei","one","360","oppo","vivo");
            for (String key : keys) {
                log.info("查询 {}",key);
                Set<Good> goodAll = searchJdGoodUtils.getGoodAll(key);
                boolean good = elasticSearchService.bulkAddData("good", goodAll);
                log.info("是否添加成功 {}",good);
                Thread.sleep(new Random().nextInt(5000)+8000);//休息
            }

        } catch (Exception e) {
            log.info("抓取数据异常：{}",e);
        }
    }

    @Override
    public List<Map<String, Object>> searchByPage(String keyword, int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1:pageNum;
        pageSize = pageSize <=0 ? 10:pageSize;
        return elasticSearchService.searchByPage("title", "good", keyword, pageNum, pageSize,false);
    }

    @Override
    public List<Map<String, Object>> searchByHighlightAndPage(String keyword, int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1:pageNum;
        pageSize = pageSize <=0 ? 10:pageSize;
        return elasticSearchService.searchByPage("title", "good", keyword, pageNum, pageSize,true);
    }
}
