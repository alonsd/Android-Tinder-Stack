package com.etiennelawlor.tinderstack.bus.events;

public interface OnCardSwipedListener {

  void send(Object object);
  void onNext(Integer integer);

}
