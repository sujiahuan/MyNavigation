package org.jiahuan.mapper.sys;

import org.jiahuan.entity.sys.SysDevice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wj
 * @since 2020-07-26
 */
public interface SysDeviceMapper extends BaseMapper<SysDevice> {

    SysDevice getSysDeviceById(Integer id);

    List<SysDevice>  getAllSysDevice();
}
