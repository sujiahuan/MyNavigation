package org.jiahuan.mapper.sys;

import org.apache.ibatis.annotations.Param;
import org.jiahuan.entity.sys.SysDeviceNavigation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jh
 * @since 2021-05-24
 */
public interface SysDeviceNavigationMapper extends BaseMapper<SysDeviceNavigation> {

    List<SysDeviceNavigation> getListByDeviceId(Integer deviceId);
}
