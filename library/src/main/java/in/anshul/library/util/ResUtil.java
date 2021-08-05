/*
 *  * Copyright (C) 2021 Huawei Device Co., Ltd.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * /
 */

package in.anshul.library.util;

import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.element.PixelMapElement;
import ohos.app.Context;
import ohos.global.resource.*;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Size;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * The type Res util.
 */
public class ResUtil {
    private static final String TAG = "ResUtil";

    private static Map<Integer, PixelMap> imageCache = new HashMap();

    private ResUtil() {
    }

    /**
     * get the path from id
     *
     * @param context the context
     * @param id      the id
     * @return the path from id
     */
    public static String getPathById(Context context, int id) {
        String path = "";
        if (context == null) {
            LogUtil.error(TAG, "getPathById -> get null context");
            return path;
        }
        ResourceManager manager = context.getResourceManager();
        if (manager == null) {
            LogUtil.error(TAG, "getPathById -> get null ResourceManager");
            return path;
        }
        try {
            path = manager.getMediaPath(id);
        } catch (IOException e) {
            LogUtil.error(TAG, "getPathById -> IOException");
        } catch (NotExistException e) {
            LogUtil.error(TAG, "getPathById -> NotExistException");
        } catch (WrongTypeException e) {
            LogUtil.error(TAG, "getPathById -> WrongTypeException");
        }
        return path;
    }

    /**
     * get the color
     *
     * @param context the context
     * @param id      the id
     * @return the color
     */
    public static int getColor(Context context, int id) {
        int result = 0;
        if (context == null) {
            LogUtil.error(TAG, "getColor -> get null context");
            return result;
        }
        ResourceManager manager = context.getResourceManager();
        if (manager == null) {
            LogUtil.error(TAG, "getColor -> get null ResourceManager");
            return result;
        }
        try {
            result = manager.getElement(id).getColor();
        } catch (IOException e) {
            LogUtil.error(TAG, "getColor -> IOException");
        } catch (NotExistException e) {
            LogUtil.error(TAG, "getColor -> NotExistException");
        } catch (WrongTypeException e) {
            LogUtil.error(TAG, "getColor -> WrongTypeException");
        }
        return result;
    }

    /**
     * get the boolean value
     *
     * @param context the context
     * @param id      the id
     * @return get the boolean dimen value
     */
    public static boolean getBoolean(Context context, int id) {
        boolean result = false;
        if (context == null) {
            LogUtil.error(TAG, "getBoolean -> get null context");
            return result;
        }
        ResourceManager manager = context.getResourceManager();
        if (manager == null) {
            LogUtil.error(TAG, "getBoolean -> get null ResourceManager");
            return result;
        }
        try {
            result = manager.getElement(id).getBoolean();
        } catch (IOException e) {
            LogUtil.error(TAG, "getBoolean -> IOException");
        } catch (NotExistException e) {
            LogUtil.error(TAG, "getBoolean -> NotExistException");
        } catch (WrongTypeException e) {
            LogUtil.error(TAG, "getBoolean -> WrongTypeException");
        }
        return result;
    }

    /**
     * get the pixel map
     *
     * @param context the context
     * @param id      the id
     * @return the pixel map
     */
    public static Optional<PixelMap> getPixelMap(Context context, int id) {
        String path = getPathById(context, id);
        if (TextUtils.isEmpty(path)) {
            LogUtil.error(TAG, "getPixelMap -> get empty path");
            return Optional.empty();
        }
        RawFileEntry assetManager = context.getResourceManager().getRawFileEntry(path);
        ImageSource.SourceOptions options = new ImageSource.SourceOptions();
        options.formatHint = "image/png";
        ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
        try {
            Resource asset = assetManager.openRawFile();
            ImageSource source = ImageSource.create(asset, options);
            return Optional.ofNullable(source.createPixelmap(decodingOptions));
        } catch (IOException e) {
            LogUtil.error(TAG, "getPixelMap -> IOException");
        }
        return Optional.empty();
    }

    /**
     * get the Pixel Map Element
     *
     * @param context the context
     * @param resId   the res id
     * @return the Pixel Map Element
     */
    public static PixelMapElement getPixelMapDrawable(Context context, int resId) {
        Optional<PixelMap> optional = getPixelMap(context, resId);
        return optional.map(PixelMapElement::new).orElse(null);
    }


    /**
     * Create by resource id pixel map.
     *
     * @param abilitySlice the ability slice
     * @param id           the id
     * @param str          the str
     * @return the pixel map
     * @throws IOException       the io exception
     * @throws NotExistException the not exist exception
     */
    public static PixelMap createByResourceId(AbilitySlice abilitySlice, int id, String str)
            throws IOException, NotExistException {
        if (abilitySlice == null) {
            LogUtil.error(TAG, "createByResourceId but slice is null");
            throw new IOException();
        } else if (imageCache.containsKey(Integer.valueOf(id))) {
            return imageCache.get(Integer.valueOf(id));
        } else {
            ResourceManager resourceManager = abilitySlice.getResourceManager();
            if (resourceManager != null) {
                Resource resource = resourceManager.getResource(id);
                if (resource != null) {
                    ImageSource.SourceOptions sourceOptions = new ImageSource.SourceOptions();
                    sourceOptions.formatHint = str;
                    ImageSource create = ImageSource.create(readResource(resource), sourceOptions);
                    resource.close();
                    if (create != null) {
                        ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
                        decodingOptions.desiredSize = new Size(0, 0);
                        decodingOptions.desiredRegion = new ohos.media.image.common.Rect(0, 0, 0, 0);
                        decodingOptions.desiredPixelFormat = PixelFormat.ARGB_8888;
                        PixelMap pixelMap = create.createPixelmap(decodingOptions);
                        imageCache.put(Integer.valueOf(id), pixelMap);
                        return pixelMap;
                    }
                    LogUtil.error(TAG, "imageSource is null");
                    throw new FileNotFoundException();
                }
                LogUtil.error(TAG, "get resource failed");
                throw new IOException();
            }
            LogUtil.error(TAG, "get resource manager failed");
            throw new IOException();
        }
    }

    private static byte[] readResource(Resource resource) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[1024];
        while (true) {
            try {
                int read = resource.read(bArr, 0, 1024);
                if (read == -1) {
                    break;
                }
                byteArrayOutputStream.write(bArr, 0, read);
            } catch (IOException e) {
                LogUtil.error(TAG, "readResource failed " + e.getLocalizedMessage());
            } finally {
                byteArrayOutputStream.close();
            }
        }
        LogUtil.debug(TAG, "readResource finish");
        LogUtil.debug(TAG, "readResource len: " + byteArrayOutputStream.size());
        return byteArrayOutputStream.toByteArray();
    }
}

