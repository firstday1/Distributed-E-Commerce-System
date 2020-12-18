package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.GoodsSpuPoMapper;
import cn.edu.xmu.goods.mapper.GrouponActivityPoMapper;
import cn.edu.xmu.goods.model.Status;
import cn.edu.xmu.goods.model.StatusWrap;
import cn.edu.xmu.goods.model.bo.GrouponActivity;
import cn.edu.xmu.goods.model.po.GoodsSpuPo;
import cn.edu.xmu.goods.model.po.GrouponActivityPo;
import cn.edu.xmu.goods.model.po.GrouponActivityPoExample;
import cn.edu.xmu.goods.model.vo.GrouponActivityInVo;
import cn.edu.xmu.goods.model.vo.GrouponActivityOutVo;
import cn.edu.xmu.goods.model.vo.GrouponActivityVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class GrouponActivityDao {
    @Autowired(required = false)
    private GrouponActivityPoMapper grouponActivityPoMapper;
    @Autowired(required = false)
    private GoodsSpuPoMapper goodsSpuPoMapper;

    public ResponseEntity<StatusWrap> createGrouponActivity(GrouponActivity grouponActivity) {
        GrouponActivityPo po = grouponActivity.getGrouponActivityPo();
        GoodsSpuPo goodsSpu = goodsSpuPoMapper.selectByPrimaryKey(po.getGoodsSpuId());
        if (goodsSpu.getShopId() != po.getShopId() && po.getShopId() != 0) {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        int ret = grouponActivityPoMapper.insert(po);
        if (ret != 0) {
            return StatusWrap.of(po);
        } else {
            return StatusWrap.just(Status.FIELD_NOTVALID);
        }
    }

    public ResponseEntity<StatusWrap> getallGrouponActivity(GrouponActivityInVo vo) {
        List<GrouponActivityPo> grouponActivityList = null;
        GrouponActivityPoExample example = new GrouponActivityPoExample();
        GrouponActivityPoExample.Criteria criteria = example.createCriteria();
        PageHelper.startPage(vo.getPage(), vo.getPageSize());
        if (vo.getState() != null) {
            if (vo.getGoodsSpuId() != null) {
                criteria.andStateEqualTo(vo.getState().byteValue()).andGoodsSpuIdEqualTo(vo.getGoodsSpuId());

            } else {
                criteria.andStateEqualTo(vo.getState().byteValue());
            }
        } else {
            if (vo.getGoodsSpuId() != null) {
                criteria.andGoodsSpuIdEqualTo(vo.getGoodsSpuId());

            } else {
                criteria.andShopIdEqualTo(vo.getShopid());
            }
        }
        try {
            grouponActivityList = grouponActivityPoMapper.selectByExample(example);
        } catch (DataAccessException e) {
            StringBuilder message = new StringBuilder().append("getGrouponActivity: ").append(e.getMessage());
        }

        if (null == grouponActivityList || grouponActivityList.isEmpty()) {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        } else {
            List<GrouponActivityOutVo> grouponActivityOutVos = grouponActivityList.stream().map(GrouponActivityOutVo::new).collect(Collectors.toList());
            PageInfo<GrouponActivityOutVo> grouponActivityOutVoPageInfo = PageInfo.of(grouponActivityOutVos);
            return StatusWrap.of(grouponActivityOutVoPageInfo);
        }
    }

    public ResponseEntity<StatusWrap> getGrouponActivity(GrouponActivityInVo vo) {
        List<GrouponActivityPo> grouponActivityList = null;
        GrouponActivityPoExample example = new GrouponActivityPoExample();
        GrouponActivityPoExample.Criteria criteria = example.createCriteria();
        PageHelper.startPage(vo.getPage(), vo.getPageSize());
        if (vo.getTimeline() != null) {
            if (vo.getGoodsSpuId() != null) {
                switch (vo.getTimeline()) {
                    case 0:
                        criteria.andGoodsSpuIdEqualTo(vo.getGoodsSpuId())
                                .andStateEqualTo(GrouponActivity.State.ONLINE.getCode().byteValue())
                                .andBeginTimeGreaterThan(LocalDateTime.now());
                    case 1:
                        criteria.andGoodsSpuIdEqualTo(vo.getGoodsSpuId())
                                .andStateEqualTo(GrouponActivity.State.ONLINE.getCode().byteValue())
                                .andBeginTimeGreaterThan(LocalDate.now().plusDays(1).atTime(LocalTime.MIN))
                                .andBeginTimeLessThan(LocalDate.now().plusDays(1).atTime(LocalTime.MAX));
                    case 2:
                        criteria.andGoodsSpuIdEqualTo(vo.getGoodsSpuId())
                                .andStateEqualTo(GrouponActivity.State.ONLINE.getCode().byteValue())
                                .andBeginTimeLessThan(LocalDateTime.now())
                                .andEndTimeGreaterThan(LocalDateTime.now());
                    case 3:
                        criteria.andGoodsSpuIdEqualTo(vo.getGoodsSpuId())
                                .andStateEqualTo(GrouponActivity.State.ONLINE.getCode().byteValue())
                                .andBeginTimeLessThan(LocalDateTime.now());
                }
            } else if (vo.getShopid() != null) {
                switch (vo.getTimeline()) {
                    case 0:
                        criteria.andShopIdEqualTo(vo.getShopid())
                                .andStateEqualTo(GrouponActivity.State.ONLINE.getCode().byteValue())
                                .andBeginTimeGreaterThan(LocalDateTime.now());
                    case 1:
                        criteria.andShopIdEqualTo(vo.getShopid())
                                .andStateEqualTo(GrouponActivity.State.ONLINE.getCode().byteValue())
                                .andBeginTimeGreaterThan(LocalDate.now().plusDays(1).atTime(LocalTime.MIN))
                                .andBeginTimeLessThan(LocalDate.now().plusDays(1).atTime(LocalTime.MAX));
                    case 2:
                        criteria.andShopIdEqualTo(vo.getShopid())
                                .andStateEqualTo(GrouponActivity.State.ONLINE.getCode().byteValue())
                                .andBeginTimeLessThan(LocalDateTime.now())
                                .andEndTimeGreaterThan(LocalDateTime.now());
                    case 3:
                        criteria.andShopIdEqualTo(vo.getShopid())
                                .andStateEqualTo(GrouponActivity.State.ONLINE.getCode().byteValue())
                                .andBeginTimeLessThan(LocalDateTime.now());
                }
            } else {
                switch (vo.getTimeline()) {
                    case 0:
                        criteria
                                .andStateEqualTo(GrouponActivity.State.ONLINE.getCode().byteValue())
                                .andBeginTimeGreaterThan(LocalDateTime.now());
                    case 1:
                        criteria
                                .andStateEqualTo(GrouponActivity.State.ONLINE.getCode().byteValue())
                                .andBeginTimeGreaterThan(LocalDate.now().plusDays(1).atTime(LocalTime.MIN))
                                .andBeginTimeLessThan(LocalDate.now().plusDays(1).atTime(LocalTime.MAX));
                    case 2:
                        criteria
                                .andStateEqualTo(GrouponActivity.State.ONLINE.getCode().byteValue())
                                .andBeginTimeLessThan(LocalDateTime.now())
                                .andEndTimeGreaterThan(LocalDateTime.now());
                    case 3:
                        criteria
                                .andStateEqualTo(GrouponActivity.State.ONLINE.getCode().byteValue())
                                .andBeginTimeLessThan(LocalDateTime.now());
                }
            }
        } else {
            if (vo.getGoodsSpuId() != null) {
                criteria.andGoodsSpuIdEqualTo(vo.getGoodsSpuId()).andStateEqualTo(GrouponActivity.State.ONLINE.getCode().byteValue());
            } else if (vo.getShopid() != null) {
                criteria.andShopIdEqualTo(vo.getShopid()).andStateEqualTo(GrouponActivity.State.ONLINE.getCode().byteValue());
            } else {
                criteria.andStateEqualTo(GrouponActivity.State.ONLINE.getCode().byteValue());
            }
        }
        try {
            grouponActivityList = grouponActivityPoMapper.selectByExample(example);
        } catch (DataAccessException e) {
            StringBuilder message = new StringBuilder().append("getGrouponActivity: ").append(e.getMessage());
        }

        if (null == grouponActivityList || grouponActivityList.isEmpty()) {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        } else {
            List<GrouponActivityOutVo> grouponActivityOutVos = grouponActivityList.stream().map(GrouponActivityOutVo::new).collect(Collectors.toList());
            PageInfo<GrouponActivityOutVo> grouponActivityOutVoPageInfo = PageInfo.of(grouponActivityOutVos);
            return StatusWrap.of(grouponActivityOutVoPageInfo);
        }
    }

    public GrouponActivityPo getGrouponActivityById(Long Id) {

        GrouponActivityPo grouponActivityPo = grouponActivityPoMapper.selectByPrimaryKey(Id);

        return grouponActivityPo;
    }

    public ResponseEntity<StatusWrap> getGrouponActivityByShopid(Long shopid) {
        GrouponActivityPoExample example = new GrouponActivityPoExample();
        GrouponActivityPoExample.Criteria criteria = example.createCriteria();
        criteria.andShopIdEqualTo(shopid);
        List<GrouponActivityPo> grouponActivityList = null;
        try {
            grouponActivityList = grouponActivityPoMapper.selectByExample(example);
        } catch (DataAccessException e) {
            StringBuilder message = new StringBuilder().append("getGrouponActivityByShopid: ").append(e.getMessage());
        }

        if (null == grouponActivityList || grouponActivityList.isEmpty()) {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        } else {
            GrouponActivityOutVo grouponActivityvo = new GrouponActivityOutVo(grouponActivityList.get(0));
            return StatusWrap.of(grouponActivityvo);
        }
    }

    public ResponseEntity<StatusWrap> getGrouponActivityBySpuid(Long spuid) {
        GrouponActivityPoExample example = new GrouponActivityPoExample();
        GrouponActivityPoExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsSpuIdEqualTo(spuid);
        List<GrouponActivityPo> grouponActivityList = null;
        try {
            grouponActivityList = grouponActivityPoMapper.selectByExample(example);
        } catch (DataAccessException e) {
            StringBuilder message = new StringBuilder().append("getGrouponActivityBySpuid: ").append(e.getMessage());
        }

        if (null == grouponActivityList || grouponActivityList.isEmpty()) {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        } else {
            GrouponActivityOutVo grouponActivityvo = new GrouponActivityOutVo(grouponActivityList.get(0));
            return StatusWrap.of(grouponActivityvo);
        }
    }

    public ResponseEntity<StatusWrap> modifyGrouponActivity(Long id, GrouponActivityVo vo) {
        GrouponActivityPo po = grouponActivityPoMapper.selectByPrimaryKey(id);

        if (po.getShopId() != vo.getShopId() && vo.getShopId() != 0) {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        if (po.getState() != GrouponActivity.State.OFFLINE.getCode().byteValue()) {
            return StatusWrap.just(Status.GROUPON_STATENOTALLOW);
        }
        po.setGmtModified(LocalDateTime.now());
        po.setStrategy(vo.getStrategy());
        po.setBeginTime(vo.getBeginTime());
        po.setEndTime(vo.getEndTime());
        int ret = grouponActivityPoMapper.updateByPrimaryKeySelective(po);

        if (ret != 0) {
            return StatusWrap.just(Status.OK);
        } else {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
    }

    public ResponseEntity<StatusWrap> GtoONLINE(Long shopId, Long id) {
        GrouponActivityPo po = grouponActivityPoMapper.selectByPrimaryKey(id);
        if (po.getShopId() != shopId && shopId != 0) {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        if (po.getState() != GrouponActivity.State.OFFLINE.getCode().byteValue()) {
            return StatusWrap.just(Status.GROUPON_STATENOTALLOW);
        }
        po.setGmtModified(LocalDateTime.now());
        po.setState(GrouponActivity.State.ONLINE.getCode().byteValue());
        int ret = grouponActivityPoMapper.updateByPrimaryKeySelective(po);

        if (ret != 0) {
            return StatusWrap.just(Status.OK);
        } else {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
    }

    public ResponseEntity<StatusWrap> GtoOFFLINE(Long shopId, Long id) {
        GrouponActivityPo po = grouponActivityPoMapper.selectByPrimaryKey(id);
        if (po.getShopId() != shopId && shopId != 0) {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        if (po.getState() != GrouponActivity.State.ONLINE.getCode().byteValue()) {
            return StatusWrap.just(Status.GROUPON_STATENOTALLOW);
        }

        po.setGmtModified(LocalDateTime.now());
        po.setState(GrouponActivity.State.OFFLINE.getCode().byteValue());
        int ret = grouponActivityPoMapper.updateByPrimaryKeySelective(po);

        if (ret != 0) {
            return StatusWrap.just(Status.OK);
        } else {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
    }

    public ResponseEntity<StatusWrap> deleteGrouponActivityById(Long shopId, Long id) {
        GrouponActivityPo po = grouponActivityPoMapper.selectByPrimaryKey(id);
        if (po.getShopId() != shopId && shopId != 0) {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        if (po.getState() != GrouponActivity.State.OFFLINE.getCode().byteValue()) {
            return StatusWrap.just(Status.GROUPON_STATENOTALLOW);
        }
        po.setGmtModified(LocalDateTime.now());
        po.setState(GrouponActivity.State.DELETE.getCode().byteValue());
        int ret = grouponActivityPoMapper.updateByPrimaryKeySelective(po);

        if (ret != 0) {
            return StatusWrap.just(Status.OK);
        } else {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
    }

}