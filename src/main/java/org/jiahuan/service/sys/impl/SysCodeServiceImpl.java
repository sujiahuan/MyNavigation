package org.jiahuan.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jiahuan.entity.coun.CounDivisor;
import org.jiahuan.entity.sys.SysDivisor;
import org.jiahuan.mapper.sys.SysDivisorMapper;
import org.jiahuan.service.coun.ICounDivisorService;
import org.jiahuan.service.sys.ISysDivisorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jh
 * @since 2020-09-29
 */
@Service
public class SysCodeServiceImpl extends ServiceImpl<SysDivisorMapper, SysDivisor> implements ISysDivisorService {

    @Autowired
    private ISysDivisorService iSysDivisorService;
    @Autowired
    private ICounDivisorService counDivisorService;

    @Override
    public void deleteById(Integer id) throws Exception {
        QueryWrapper<CounDivisor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code_id", id);
        List<CounDivisor> divisors = counDivisorService.list(queryWrapper);

        if(divisors.size()!=0){
            throw new Exception("当前因子已被使用，请删除关联");
        }

        iSysDivisorService.removeById(id);
    }
}