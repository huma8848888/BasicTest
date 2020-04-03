package com.example.basictest;

public abstract class BaseMessageEvent {

    private int innerCode;//内部事件代码

    public BaseMessageEvent() {
    }

    //分派给每个事件自己来处理
    abstract void actionHandler();

}
