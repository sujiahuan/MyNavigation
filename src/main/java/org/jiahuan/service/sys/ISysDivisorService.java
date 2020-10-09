package org.jiahuan.service.sys;

import org.jiahuan.entity.sys.SysDivisor;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jh
 * @since 2020-09-29
 */
public interface ISysDivisorService extends IService<SysDivisor> {

    void deleteById(Integer id) throws Exception;
}
