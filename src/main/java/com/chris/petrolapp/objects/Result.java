package com.chris.petrolapp.objects;

import java.util.List;

public class Result<K> {

  public String cursor;
  public List<K> items;

  public Result(List<K> items, String cursor) {
    this.items = items;
    this.cursor = cursor;
  }

  public Result(List<K> items) {
    this.items = items;
    this.cursor = null;
  }
}