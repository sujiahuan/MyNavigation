package org.jiahuan.mapper.sys;

import org.jiahuan.entity.sys.SysNavigation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wj
 * @since 2020-06-16
 */
public interface SysNavigationMapper extends BaseMapper<SysNavigation> {

    List<SysNavigation> getByDeviceId(Integer deviceId);
}
