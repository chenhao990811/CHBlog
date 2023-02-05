package com.chcode.utils;

import com.chcode.domain.entity.Article;
import com.chcode.domain.vo.HotArticleVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

// bean拷贝工具类
public class BeanCopyUtils {

    private BeanCopyUtils() {
    }

    // 拷贝单个对象（泛型方法）
    public static <V> V copyBean(Object source,Class<V> clazz) {
        //创建目标对象clazz ,通过反射
        V result = null;
        try {
            result = clazz.newInstance();
            //实现属性copy
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回结果
        return result;
    }
    // 拷贝集合
    public static <O,V> List<V> copyBeanList(List<O> list, Class<V> clazz){
        /**
         * map将Stream流中的O类型的数据，转化为了V类型的数据（O，V为参数泛型）
         * collect将Stream<V>流转化为了一个List<V>集合
         */
//        List<V> collect = list.stream()
//                .map(o -> copyBean(o, clazz))
//                .collect(Collectors.toList());
        return list.stream()
                .map(o -> copyBean(o, clazz))
                .collect(Collectors.toList());
    }


//    public static void main(String[] args) {
//        // 测试单个对象拷贝是否成功
//        Article article = new Article();
//        article.setId(4L);
//        article.setTitle("test copy");
//        HotArticleVo hotArticleVo = copyBean(article, HotArticleVo.class);
//        System.out.println(hotArticleVo);
//    }
}
