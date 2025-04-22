package com.example.spba.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.spba.domain.entity.User;
import com.example.spba.domain.entity.WithdrawRequest;
import com.example.spba.service.UserService;
import com.example.spba.service.WithDrawRequsetService;
import com.example.spba.service.impl.UserServiceImpl;
import com.example.spba.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/withdraw")
public class WithDrawRequestController {

    @Autowired
    private WithDrawRequsetService withDrawRequsetService;

    @Autowired
    private UserService userService;

    @GetMapping("/getRequests")
    public R getRequests(@RequestParam Integer pageNum,
                         @RequestParam Integer pageSize) {
        // Logic to get all withdrawal requests
        IPage<WithdrawRequest> requests = withDrawRequsetService.getRequests(pageNum, pageSize);
        return R.success(requests);
    }

    @PostMapping("/generate")
    public R generate(@RequestBody WithdrawRequest withdrawRequest) {
        // Logic to generate a withdrawal request
        try {
            int loginIdAsInt = StpUtil.getLoginIdAsInt();
            withdrawRequest.setUserId(loginIdAsInt);
            System.out.println("WithdrawRequest: " + withdrawRequest);
            withDrawRequsetService.save(withdrawRequest);
            return R.success("Withdrawal request generated successfully");
        } catch (Exception e) {
            return R.error("Failed to generate withdrawal request: " + e.getMessage());
        }
    }

    @PostMapping("/confirm")
    public R confirm(@RequestParam int id) {
        // Logic to confirm a withdrawal request
        try {
            WithdrawRequest withdrawRequest = withDrawRequsetService.getById(id);
            if (withdrawRequest == null) {
                return R.error("Withdrawal request not found");
            }
            // Assuming the status is updated to "confirmed"
            withdrawRequest.setStatus("已通过");
            withDrawRequsetService.updateById(withdrawRequest);
            // Logic to update the user's balance or any other necessary actions
            // For example:
             User user = userService.getById(withdrawRequest.getUserId());
             user.setWealth(user.getWealth().subtract(withdrawRequest.getAmount()));
             userService.updateById(user);
            return R.success("Withdrawal request confirmed successfully");
        } catch (Exception e) {
            return R.error("Failed to confirm withdrawal request: " + e.getMessage());
        }
    }
}
