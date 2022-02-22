package com.myz.mybatis.pagehelper;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.myz.dao.UserVOMapper;
import com.myz.entity.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author maoyz
 * @version V1.0
 * @date 2022/2/21 19:43
 */
@Service
public class UserServicePage {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserVOMapper userVOMapper;

    /**
     * 设置不查询总数.count(false)，不执行SELECT count(0)
     *
     * @return
     */
    public List<UserVO> pageNoCount(int pageNum, int pageSize) {
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).count(false).doSelectPageInfo(() -> {
            userVOMapper.getAll();
        });
        // -1, 10
        logger.info("total = {}, listSize = {}, pageInfo = {}", pageInfo.getTotal(), pageInfo.getList().size(), pageInfo);
        return null;
    }

    /**
     * 默认查询总数.count(true) SELECT count(0)
     *
     * @return
     */
    public List<UserVO> page(int pageNum, int pageSize) {
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            userVOMapper.getAll();
        });
        // 11, 10
        logger.info("total = {}, listSize = {}, pageInfo = {}", pageInfo.getTotal(), pageInfo.getList().size(), pageInfo);
        return null;
    }

    public List<UserVO> pageInfo(int pageNum, int pageSize) {
        Page<Object> page = PageHelper.startPage(pageNum, pageSize);
        // 此时的all 属于Page
        List<UserVO> all = userVOMapper.getAll();
        PageInfo<UserVO> pageInfo = new PageInfo<>(all);
        logger.info("page = {}", page);
        logger.info("total = {}, listSize = {}, pageInfo = {}", pageInfo.getTotal(), pageInfo.getList().size(), pageInfo);
        return null;
    }

    /**
     * 若先对结果做处理，在设置PageInfo，total数量错误
     *
     * @return
     */
    public List<UserVO> pageInfoCopy(int pageNum, int pageSize) {
        Page<Object> page = PageHelper.startPage(pageNum, pageSize);
        List<UserVO> all = userVOMapper.getAll();
        // 后对list数据操作,可以分页，但是数据量错误，total始终等于每页数据量，即pageSize
        List<UserVO> copy = new ArrayList<>();
        for (UserVO userVO : all) {
            UserVO vo = new UserVO();
            vo.setId(userVO.getId());
            copy.add(vo);
        }
        PageInfo<UserVO> pageInfo = new PageInfo<>(copy);
        logger.info("page = {}", page);
        // 5, 5
        logger.info("total = {}, listSize = {}, pageInfo = {}", pageInfo.getTotal(), pageInfo.getList().size(), pageInfo);
        return null;
    }

    /**
     * 当SELECT count(0) = 0时，不会在执行后续的查询动作
     *
     * @return
     */
    public List<UserVO> pageInfoCount0(Long id, int pageNum, int pageSize) {
        // SELECT count(0) FROM t_user_1 ... count=0
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> userVOMapper.getUser(id));
        logger.info("total = {}, listSize = {}, pageInfo = {}", pageInfo.getTotal(), pageInfo.getList().size(), pageInfo);
        return null;
    }
}
