/**
 * 软件名称:android无限级树源码
 * 作者：shaolong
 * 网址:http://londroid.5d6d.com
 */

package com.winning.pregnancy.model;

import java.io.Serializable;

/**
 * 树节点
 * 
 * @author LongShao Download by http://www.codefans.net
 */
public class Node implements Serializable
{
    /**
	 * 
	 */
    private static final long serialVersionUID = 6470122057067581501L;
    private String id;// 主键
    private String code;// 编号
    private String text;// 节点显示的文字
    private String value;// 节点的值
    private boolean isChecked = false;// 是否处于选中状态

    /**
     * Node构造函数
     * 
     * @param text
     *            节点显示的文字
     * @param value
     *            节点的值
     */
    public Node(String text, String value)
    {
        this.text = text;
        this.value = value;
    }

    public Node()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * 设置节点文本
     * 
     * @param text
     */
    public void setText(String text)
    {
        this.text = text;
    }

    /**
     * 获得节点文本
     * 
     * @return
     */
    public String getText()
    {
        return this.text;
    }

    /**
     * 设置节点值
     * 
     * @param value
     */
    public void setValue(String value)
    {
        this.value = value;
    }

    /**
     * 获得节点值
     * 
     * @return
     */
    public String getValue()
    {
        return this.value;
    }

    /**
     * 设置节点选中状态
     * 
     * @param isChecked
     */
    public void setChecked(boolean isChecked)
    {
        this.isChecked = isChecked;
    }

    /**
     * 获得节点选中状态
     * 
     * @return
     */
    public boolean isChecked()
    {
        return isChecked;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

}
