package in.anshul.library;

import in.anshul.library.util.ResUtil;
import in.anshul.library.util.TextUtils;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.InputAttribute;
import ohos.agp.components.TextField;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.PixelMapElement;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.Texture;
import ohos.agp.utils.Color;
import ohos.app.Context;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Size;
import ohos.multimodalinput.event.MmiPoint;
import ohos.multimodalinput.event.TouchEvent;

public class PasswordEditText extends TextField implements Component.TouchEventListener {

    // custom attributes
    private static final String PASSWORD_VISIBLE = "password_visible";
    private static final String SHOW_AS_TEXT = "show_as_text";
    private static final String SHOW_TEXT = "show_text";
    private static final String HIDE_TEXT = "hide_text";
    private static final String HIDE_DRAWABLE = "hide_drawable";
    private static final String SHOW_DRAWABLE = "show_drawable";

    private final int EXTRA_TOUCH_AREA = 150;
    private Element mHideDrawable;
    private Element mShowDrawable;
    private boolean mPasswordVisible = false;
    private boolean touchDown;
    private boolean mShowAsText;
    private String mHideTextString;
    private String mShowTextString;

    public PasswordEditText(Context context) {
        super(context);
    }

    public PasswordEditText(Context context, AttrSet attrSet) {
        super(context, attrSet);
        init(context, attrSet);
    }

    public PasswordEditText(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        init(context, attrSet);
    }

    private void init(Context context, AttrSet attrs) {

        Element defaultHideDrawable = ResUtil.getPixelMapDrawable(getContext(), ResourceTable.Media_in_anshul_hide_password);
        Element defaultShowDrawable = ResUtil.getPixelMapDrawable(getContext(), ResourceTable.Media_in_anshul_show_password);

        final boolean defaultPasswordVisibility = ResUtil.getBoolean(context,
                ResourceTable.Boolean_password_edit_text_password_visible);
        final boolean defaultShowAsText = ResUtil.getBoolean(context,
                ResourceTable.Boolean_password_edit_text_show_as_text);

        mPasswordVisible = attrs.getAttr(PASSWORD_VISIBLE).isPresent() ?
                attrs.getAttr(PASSWORD_VISIBLE).get().getBoolValue() : defaultPasswordVisibility;
        mHideDrawable = attrs.getAttr(HIDE_DRAWABLE).isPresent() ?
                attrs.getAttr(HIDE_DRAWABLE).get().getElement() : defaultHideDrawable;
        mShowDrawable = attrs.getAttr(SHOW_DRAWABLE).isPresent() ?
                attrs.getAttr(SHOW_DRAWABLE).get().getElement() : defaultShowDrawable;
        mShowAsText = attrs.getAttr(SHOW_AS_TEXT).isPresent() ?
                attrs.getAttr(SHOW_AS_TEXT).get().getBoolValue() : defaultShowAsText;

        if (mShowAsText) {
            mHideTextString = attrs.getAttr(HIDE_TEXT).get().getStringValue();
            mHideTextString = TextUtils.isEmpty(mHideTextString) ?
                    getContext().getString(ResourceTable.String_hide_text) : mHideTextString;
            mShowTextString = attrs.getAttr(SHOW_TEXT).get().getStringValue();
            mShowTextString = TextUtils.isEmpty(mShowTextString) ?
                    getContext().getString(ResourceTable.String_show_text) : mShowTextString;
        }

        mHideDrawable.setBounds(0, 0, 50, 40);
        mShowDrawable.setBounds(0, 0, 50, 40);

        if (mPasswordVisible) {
            showPassword();
        } else {
            hidePassword();
        }

        setTouchEventListener(this::onTouchEvent);
    }

    private void togglePasswordView() {
        if (mPasswordVisible) {
            hidePassword();
        } else {
            showPassword();
        }
    }

    private void showPassword() {
        if (mShowAsText) {
            PixelMapElement drawable = new PixelMapElement(createTextBitmap(mHideTextString));
            drawable.setBounds(0, 0, 100, 40);
            setAroundElements(null, null, drawable, null);
        } else {
            setAroundElements(null, null, mHideDrawable, null);
        }
        setTextInputType(InputAttribute.PATTERN_TEXT);
        //setSelection(getText().length());
        mPasswordVisible = true;
    }

    private void hidePassword() {
        if (mShowAsText) {
            PixelMapElement drawable = new PixelMapElement(createTextBitmap(mShowTextString));
            drawable.setBounds(0, 0, 100, 40);
            setAroundElements(null, null, drawable, null);
        } else {
            setAroundElements(null, null, mShowDrawable, null);
        }
        setTextInputType(InputAttribute.PATTERN_PASSWORD);
        //setSelection(getText().length());
        mPasswordVisible = false;
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        final int right = getRight();
        final int drawableSize = getPaddingRight();
        MmiPoint mmiPoint = touchEvent.getPointerPosition(touchEvent.getIndex());
        final int x = (int) mmiPoint.getX();
        switch (touchEvent.getAction()) {
            case TouchEvent.PRIMARY_POINT_DOWN:
                if (x + EXTRA_TOUCH_AREA >= right - drawableSize && x <= right + EXTRA_TOUCH_AREA) {
                    touchDown = true;
                    return true;
                }
                break;
            case TouchEvent.PRIMARY_POINT_UP:
                if (x + EXTRA_TOUCH_AREA >= right - drawableSize && x <= right + EXTRA_TOUCH_AREA && touchDown) {
                    touchDown = false;
                    togglePasswordView();
                    return true;
                }
                touchDown = false;
                break;

        }

        return false;
    }

    public static PixelMap createTextBitmap(String text) {
        PixelMap.InitializationOptions options = new PixelMap.InitializationOptions();
        options.size = new Size(120, 50);
        options.pixelFormat = PixelFormat.ARGB_8888;

        final PixelMap bitmap = PixelMap.create(options);
        Texture texture = new Texture(bitmap);
        final Canvas canvas = new Canvas(texture);
        Paint paint = new Paint() {
            {
                setAntiAlias(true);
                setColor(Color.BLACK);
                setFakeBoldText(true);
                setTextSize(40);
            }
        };

        canvas.drawText(paint, text, 0, 45);
        return texture.getPixelMap();
    }

}
