package com.prance.teacher.features.redpackage.view.red;

import com.prance.teacher.R;

import org.reactivestreams.Publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.BiFunction;
import kotlin.jvm.functions.Function2;

public class test {
    public static void main(String[] args) {
        Flowable.zip(
                Flowable.create(new FlowableOnSubscribe<Publisher<Integer>>() {
                    @Override
                    public void subscribe(FlowableEmitter<Publisher<Integer>> emitter) throws Exception {

                    }
                }, BackpressureStrategy.BUFFER),
                Flowable.create(new FlowableOnSubscribe<Publisher<Integer>>() {
                    @Override
                    public void subscribe(FlowableEmitter<Publisher<Integer>> emitter) throws Exception {

                    }
                }, BackpressureStrategy.BUFFER),
                new BiFunction<Publisher<Integer>, Publisher<Integer>, Object>() {
                    @Override
                    public Object apply(Publisher<Integer> integerPublisher, Publisher<Integer> integerPublisher2) throws Exception {
                        return null;
                    }
                }
        );
    }
}
