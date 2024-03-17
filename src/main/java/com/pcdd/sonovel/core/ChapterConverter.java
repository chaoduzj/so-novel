package com.pcdd.sonovel.core;

import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import cn.hutool.http.HtmlUtil;
import com.pcdd.sonovel.model.NovelChapter;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pcdd
 */
@UtilityClass
public class ChapterConverter {

    public NovelChapter convert(NovelChapter novelChapter, String extName) {
        // 默认为 html 格式
        String content = ChapterFilter.filterAds(novelChapter.getContent());

        // txt 格式
        if ("txt".equals(extName)) {
            content = novelChapter.getTitle() + HtmlUtil.cleanHtmlTag(content).replace("&nbsp;", " ");
        }
        // epub 格式
        if ("epub".equals(extName)) {
            TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("templates", TemplateConfig.ResourceMode.CLASSPATH));
            // 符合 epub 标准的模板
            Template template = engine.getTemplate("chapter_epub.flt");
            Map<String, String> map = new HashMap<>();
            map.put("title", novelChapter.getTitle());
            // 构建符合 epub 标准的正文格式
            content = content.replace("&nbsp;&nbsp;&nbsp;&nbsp;", "<p>")
                    .replaceAll("<br>\\s*<br>", "</p>")
                    .replaceAll("\\s+", "");
            map.put("content", content);
            content = template.render(map);
        }


        novelChapter.setContent(content);
        return novelChapter;
    }

}