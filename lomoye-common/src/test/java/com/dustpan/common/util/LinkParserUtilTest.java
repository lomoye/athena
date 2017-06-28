package com.lomoye.common.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by tommy on 2016/7/11.
 */
public class LinkParserUtilTest {

    @Test
    public void testGetTaobaoItemId() throws Exception {
        Long itemId = LinkParserUtil.getTaobaoItemId("");
        Assert.assertTrue(itemId == null);

        itemId = LinkParserUtil.getTaobaoItemId(null);
        Assert.assertTrue(itemId == null);

        itemId = LinkParserUtil.getTaobaoItemId("taobao.com");
        Assert.assertTrue(itemId == null);

        itemId = LinkParserUtil.getTaobaoItemId("https://taobao.com");
        Assert.assertTrue(itemId == null);

        itemId = LinkParserUtil.getTaobaoItemId("http:/taobao.com");
        Assert.assertTrue(itemId == null);

        itemId = LinkParserUtil.getTaobaoItemId("https://item.taobao.com/item.htm?id=39005283227");
        Assert.assertTrue(itemId != null && itemId == 39005283227L);

        itemId = LinkParserUtil.getTaobaoItemId("https://item.taobao.com/item.htmid=39005283227");
        Assert.assertTrue(itemId == null);

        itemId = LinkParserUtil.getTaobaoItemId("https://detail.tmall.com/item.htm?id=536687325753");
        Assert.assertTrue(itemId != null && itemId == 536687325753L);

        itemId = LinkParserUtil.getTaobaoItemId("https://guang.taobao.com/detail/index.htm?spm=0.0.0.0.ue7cNp&uid=348075266&sid=7965508646&itemid=528649863961");
        Assert.assertTrue(itemId != null && itemId == 528649863961L);

        itemId = LinkParserUtil.getTaobaoItemId("https://item.taobao.com/item.htm?id=524479513456&ali_trackid=17_123036a785b99e02991dbd7cb1eb6c35&spm=a21bo.50862.201880.1.7Zc1Id");
        Assert.assertTrue(itemId != null && itemId == 524479513456L);

        itemId = LinkParserUtil.getTaobaoItemId("https://detail.ju.taobao.com/home.htm?spm=608.5847457.318.4.oxGKOD&itemId=536687325754&id=10000031155738");
        Assert.assertTrue(itemId != null && itemId == 536687325754L);

    }
}