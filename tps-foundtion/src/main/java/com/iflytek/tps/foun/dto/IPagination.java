package com.iflytek.tps.foun.dto;

import com.google.common.collect.Lists;

import java.util.List;

@SuppressWarnings("unused")
public class IPagination<T> {
	private int pager;

	private int pages;

	private int size;

	private long total;

	private int offset;

	private List<T> list = Lists.newArrayList();

	@SuppressWarnings("unused")
	public IPagination() {
	}

	private IPagination(int pager, int size) {
		if(pager < 0 || size <= 0){
			throw new RuntimeException("invalid pager: " + pager + " or size: " + size);
		}
		this.pager = pager;
		this.size = size;
	}

	public static IPagination create(int pager, int size){
		return new IPagination(pager, size);
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getPager() {
		return 0 == pager ? 1 : pager;
	}

	public int getSize() {
		return size;
	}

	public int getPages() {
		return Double.valueOf(Math.ceil((double)total / (double)size)).intValue();
	}

	public int getOffset() {
		return size * (getPager() -1);
	}

	public List<T> getList() {
		return list;
	}

	public long getTotal() {
		return total;
	}

}