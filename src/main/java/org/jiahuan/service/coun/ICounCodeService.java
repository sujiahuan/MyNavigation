package org.jiahuan.service.coun;

import org.jiahuan.entity.coun.CounCode;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jh
 * @since 2020-08-05
 */
public interface ICounCodeService extends IService<CounCode> {

    CounCode getCounCodeByDeviceId(Integer deviceId);

    CounCode getCounCodeByCode(String coode);

    void deleteCounCodeByDeviceId(Integer deviceId);
}
