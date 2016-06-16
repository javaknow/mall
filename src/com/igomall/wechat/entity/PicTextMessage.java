package com.igomall.wechat.entity;


public class PicTextMessage extends BaseMessage {

	private static final long serialVersionUID = 2016653786257150388L;

    private Integer ArticleCount;
    
    private Article Articles;

	public Article getArticles() {
		return Articles;
	}

	public void setArticles(Article articles) {
		Articles = articles;
	}

	public Integer getArticleCount() {
		return ArticleCount;
	}

	public void setArticleCount(Integer articleCount) {
		ArticleCount = articleCount;
	}
    
    
}
