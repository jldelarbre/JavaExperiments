package com.github.jldelarbre.javaExperiments.observer;

import com.github.jldelarbre.javaExperiments.observer.observer.IEvents;

public interface ITotosEvents2 extends IEvents {

    void eventCommon();

    void eventYoupla2();

    void eventYouplaBoom2(String someParam);

    void eventYoupla1(int kiki, Integer titi);

    void eventYouplaBoom1(Integer bibi);
}
