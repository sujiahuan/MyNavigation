package org.jiahuan.service.coun;

import org.jiahuan.entity.coun.CounCodeParameter;
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
public interface ICounCodeParameterService extends IService<CounCodeParameter> {

    List<CounCodeParameter> getCounParameterByCodeId(Integer codeId);

    void deleteByCodeId(Integer codeId);

}
