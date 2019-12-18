package com.cwc.mylibrary.takephoto.permission;

import com.cwc.mylibrary.takephoto.model.InvokeParam;

/**
 * 授权管理回调
 */
public interface InvokeListener {
    PermissionManager.TPermissionType invoke(InvokeParam invokeParam);
}
