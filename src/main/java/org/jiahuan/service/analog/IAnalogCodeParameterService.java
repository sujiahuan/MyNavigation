package org.jiahuan.service.analog;

import org.jiahuan.entity.analog.AnalogCodeParameter;
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
public interface IAnalogCodeParameterService extends IService<AnalogCodeParameter> {

    List<AnalogCodeParameter> getCounParameterByCodeId(Integer codeId);

    void deleteByCodeId(Integer codeId);

}
