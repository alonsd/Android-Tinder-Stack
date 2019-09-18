package com.etiennelawlor.tinderstack.bus.events;

public interface Completion {

  void send(Object object);
  void onNext(Integer integer);

}
