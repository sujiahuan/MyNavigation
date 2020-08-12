package org.jiahuan.service.coun;

import org.jiahuan.entity.coun.CounDivisor;
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
public interface ICounDivisorService extends IService<CounDivisor> {

    List<CounDivisor> getCounDivisorByDeviceId(Integer deviceId);

    void deleteByDeviceId(Integer deviceId);
}
