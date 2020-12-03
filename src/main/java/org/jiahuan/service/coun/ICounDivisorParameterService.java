package org.jiahuan.service.coun;

import org.jiahuan.entity.coun.CounDivisorParameter;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wj
 * @since 2020-07-26
 */
public interface ICounDivisorParameterService extends IService<CounDivisorParameter> {

    List<CounDivisorParameter> getCounDivisorByDeviceId(Integer deviceId);

    void deleteByDeviceId(Integer deviceId);
}
