package nl.d2n.controller;

import nl.d2n.service.LeaderboardService;
import nl.d2n.util.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("leaderboard")
public class LeaderboardController extends AbstractController {

    @Autowired
    private LeaderboardService leaderboardService;

    @RequestMapping(method = RequestMethod.GET, value= "unique_distinctions")
    public void getDistinctions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        writeJsonStringToResponse(GsonUtil.objectToJson(leaderboardService.findUniqueDistinctions()), response);
    }

    @RequestMapping(method = RequestMethod.GET, value= "top_100")
    public void findTopUsersWithDistinction(HttpServletRequest request, HttpServletResponse response,
                                            @RequestParam(value = "distinction_id", required = true) Integer uniqueDistinctionId
                                            ) throws IOException {
        writeJsonStringToResponse(GsonUtil.objectToJson(
                leaderboardService.findTopUsersWithDistinction(uniqueDistinctionId)), response);
    }

}
