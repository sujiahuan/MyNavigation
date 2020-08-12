package org.jiahuan.service.coun;

import org.jiahuan.entity.coun.CounCode;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jiahuan.entity.coun.CounParameter;

import java.util.List;

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


    void deleteCounCodeByDeviceId(Integer deviceId);
}
