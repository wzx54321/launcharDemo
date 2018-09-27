package com.xin.launcher.P;
// 正确的项目

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.xin.launcher.M.ModelHome;
import com.xin.launcher.activities.ConstructionActivity;
import com.xin.launcher.bean.MyAppItem;
import com.xin.launcher.fragments.HomeAppFragment;
import com.xin.launcher.utils.JsonParser;
import com.xin.launcher.widget.springboard.SpringboardAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import xin.framework.content.SPManager;
import xin.framework.mvp.PresentImpl;
import xin.framework.utils.android.BitmapLruCacheUtil;
import xin.framework.utils.android.BlurUtils;
import xin.framework.utils.android.ContextUtils;
import xin.framework.utils.android.ImageResizer;
import xin.framework.utils.android.ScreenUtils;
import xin.framework.utils.android.SysUtils;

/**
 * 作者：xin on 2018/9/21 10:26
 * <p>
 * 邮箱：ittfxin@126.com
 */
public class PHome extends PresentImpl<HomeAppFragment> implements SpringboardAdapter.DataChangeListener<MyAppItem> {

    private ModelHome model;
    private View.OnTouchListener mOnTouchListener;
    private SpeechRecognizer mIat;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private boolean mTranslateEnable;
    // 用HashMap存储听写结果

    private Handler mHandler;
    private Runnable mSpeechRunnable;

    /**
     * 初始化科大讯飞
     */
    private void initSpeech() {
        if (mIat == null) {

            mIat = SpeechRecognizer.createRecognizer(getV().getContext(), new InitListener() {
                @Override
                public void onInit(int code) {
                    if (code != ErrorCode.SUCCESS) {
                        xin.framework.utils.android.Loger.Log.e("语音初始化失败，错误码：" + code);
                    }
                }
            });


        }

    }

    public PHome() {
        model = new ModelHome();
        mHandler = new Handler();

        mSpeechRunnable = new Runnable() {
            @Override
            public void run() {
                if (SysUtils.hasM()) {
                    if (PackageManager.PERMISSION_GRANTED ==
                            Objects.requireNonNull(getV().getActivity()).
                                    checkSelfPermission(Manifest.permission.RECORD_AUDIO)) {
                        startSpeech();
                    } else {
                        getV().getDelegateHandler().toastLong("请开启语音权限");
                        mHandler.removeCallbacks(this);
                    }
                } else {
                    startSpeech();
                }
            }
        };
    }


    public List<MyAppItem> getApps() {

        List<MyAppItem> apps = model.getAppFromDB();

        if (apps == null || apps.isEmpty()) {
            apps = model.getAppFromNet();
            model.getAppBox().deleteAll();
            model.getAppBox().insert(apps);
        }

        return apps;
    }

    public SpringboardAdapter.DataChangeListener<MyAppItem> getDataChangeListener() {
        return this;
    }


    @Override
    public void onDeleteItem(ArrayList<MyAppItem> myAppItems, MyAppItem app) {// ok
        model.deleteItem(myAppItems, app);
        onDataChanged();
    }

    @Override
    public void onExchangeItem(ArrayList<MyAppItem> myAppItems) {// ok
        model.exchangeItem(myAppItems);
        onDataChanged();
    }

    @Override
    public void onChangeFolderName(MyAppItem app) {// ok
        model.changeFolderName(app);
        onDataChanged();

    }

    @Override
    public void onExChangeSubItem(MyAppItem folder) {// ok
        model.exChangeSubItem(folder);
        onDataChanged();
    }


    @Override
    public void onMergeItem(boolean needCreateId, MyAppItem toItem, MyAppItem fromItem, ArrayList<MyAppItem> items) {//ok
        model.mergeItem(needCreateId, toItem, fromItem, items);
        onDataChanged();
    }

    @Override
    public void onDeleteFolderItem(MyAppItem folder, ArrayList<MyAppItem> myAppItems, MyAppItem itemBeRemove) {// ok
        model.deleteFolderItem(folder, myAppItems, itemBeRemove);
        onDataChanged();
    }

    @Override
    public void addItem(ArrayList<MyAppItem> myAppItems) {// ok
        model.addItem(myAppItems);
        onDataChanged();
    }


    @Override
    public void addItemToFolder(boolean needCreateId, ArrayList<MyAppItem> items, MyAppItem folder, MyAppItem app) {
        model.addItemToFolder(needCreateId, items, folder, app);
        onDataChanged();
    }


    /**
     * 创建高斯模糊背景
     *
     * @param context
     */
    public Observable<Bitmap> createBlurBg(final Context context, final @DrawableRes int bgRes) {
        return createBlurBg(context, bgRes, 25);
    }


    public Observable<Bitmap> createBlurBg(final Context context, final @DrawableRes int bgRes, final int radius) {
        return Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> emitter) {

                Bitmap bitmap = new ImageResizer(context, ScreenUtils.getScreenInfo()[0], ScreenUtils.dp2px(60)).
                        processBitmap(bgRes);
                int mRadius = radius > 25 ? 25 : radius;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    bitmap = BlurUtils.rsBlur(context, bitmap, mRadius);
                } else {
                    bitmap = BlurUtils.blur(bitmap, mRadius);
                }
                emitter.onNext(bitmap);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<Bitmap> saveBlurBg(final Bitmap bgRes, final int page) {
        return Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> emitter) {
                Bitmap bitmap;
                try {
                    Bitmap bitShot = ImageResizer.scaledBitmap(bgRes,
                            ScreenUtils.getScreenInfo()[0] / 3,
                            ScreenUtils.getScreenInfo()[1] / 3);
                    BitmapLruCacheUtil.addBitmapToMemoryCache(page + "screen_shot", bitShot);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        bitmap = BlurUtils.rsBlur(ContextUtils.getContext(), bitShot, 25);
                    } else {
                        bitmap = BlurUtils.blur(bitShot, 25);
                    }
                    if (bitmap != null)
                        BitmapLruCacheUtil.addBitmapToMemoryCache(page + "screen_shot_blur", bitmap);
                } catch (Exception e) {

                }

                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());
    }


    public View.OnTouchListener getOnTouchListener() {
        if (mOnTouchListener == null) {
            mOnTouchListener = new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    long endTime;
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN://0
                            mHandler.postDelayed(mSpeechRunnable, 1000);
                            getV().setAudioBg(true);

                            break;
                        case MotionEvent.ACTION_CANCEL://3
                        case MotionEvent.ACTION_UP://1
                            mHandler.removeCallbacks(mSpeechRunnable);
                            getV().setAudioBg(false);

                            endTime = event.getEventTime();
                            if (endTime - event.getDownTime() > 800) {
                                // 停止语音识别
                                stopSpeech();
                            }
                            break;
                    }


                    return true;
                }
            };
        }
        return mOnTouchListener;
    }

    private void stopSpeech() {
        if (mIat == null) return;
        if (mIat.isListening()) {
            mIat.stopListening();
        }
    }

    private void startSpeech() {


        initSpeech();
        setParam();
        if (mIat != null)
            mIat.startListening(new RecognizerListener() {
                @Override
                public void onVolumeChanged(int i, byte[] bytes) {
                }

                @Override
                public void onBeginOfSpeech() {
                    getV().getDelegateHandler().toastShort("开始识别");
                }

                @Override
                public void onEndOfSpeech() {
                    getV().getDelegateHandler().toastShort("识别结束");
                }

                @Override
                public void onResult(RecognizerResult results, boolean isLast) {
                    if (mTranslateEnable) {
                        printTransResult(results);
                    } else {
                        printResult(results);
                    }

                    if (isLast) {

                    }
                }

                @Override
                public void onError(SpeechError speechError) {

                }

                @Override
                public void onEvent(int i, int i1, int i2, Bundle bundle) {

                }
            });
    }


    /**
     * 参数设置
     *
     * @return
     */
    public void setParam() {
        if (mIat == null)
            return;
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        this.mTranslateEnable = SPManager.getInstance().getBoolean("translate", false);
        if (mTranslateEnable) {
            mIat.setParameter(SpeechConstant.ASR_SCH, "1");
            mIat.setParameter(SpeechConstant.ADD_CAP, "translate");
            mIat.setParameter(SpeechConstant.TRS_SRC, "its");
        }

        String lag = SPManager.getInstance().getString("iat_language_preference",
                "mandarin");
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
            mIat.setParameter(SpeechConstant.ACCENT, null);

            if (mTranslateEnable) {
                mIat.setParameter(SpeechConstant.ORI_LANG, "en");
                mIat.setParameter(SpeechConstant.TRANS_LANG, "cn");
            }
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);

            if (mTranslateEnable) {
                mIat.setParameter(SpeechConstant.ORI_LANG, "cn");
                mIat.setParameter(SpeechConstant.TRANS_LANG, "en");
            }
        }
        //此处用于设置dialog中不显示错误码信息
        //mIat.setParameter("view_tips_plain","false");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }


    private void printTransResult(RecognizerResult results) {
        String trans = JsonParser.parseTransResult(results.getResultString(), "dst");
        String oris = JsonParser.parseTransResult(results.getResultString(), "src");

        if (TextUtils.isEmpty(trans) || TextUtils.isEmpty(oris)) {
            /*  showTip( "解析结果失败，请确认是否已开通翻译功能。" );*/// TODO
        } else {
            /*mResultText.setText( "原始语言:\n"+oris+"\n目标语言:\n"+trans );*/ // TODO
        }

    }

    @SuppressLint("CheckResult")
    private void printResult(final RecognizerResult results) {

        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) {
                String result = model.parserAudioResult(results);
                List<MyAppItem> items = getV().getAppList();
                if (items != null && !TextUtils.isEmpty(result)) {
                    for (MyAppItem app : items) {
                        MyAppItem appItem = app.matchName(result);
                        if (appItem != null) {
                            emitter.onNext(appItem);
                        }
                    }
                }
                emitter.onNext(new Object());

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object appItem) {

                if (appItem instanceof MyAppItem) {
                    //   跳转
                    ConstructionActivity.launcher(ContextUtils.getContext(), ((MyAppItem) appItem).getName());
                } else {
                    //   提示
                    getV().getDelegateHandler().toastShort("没有查询到");
                }
            }
        });


    }


    private void onDataChanged() {
        // 处理高斯模糊的背景
        int page = getV().getMyAdapter().getSpringboardView().getmCurScreen();
        BitmapLruCacheUtil.clearCacheByKey(page + "screen_shot");
        BitmapLruCacheUtil.clearCacheByKey(page + "screen_shot_blur");
        Bitmap bitShot = ScreenUtils.shotScreen(getV().getActivity());
        saveBlurBg(bitShot, page).subscribe();

    }
}
