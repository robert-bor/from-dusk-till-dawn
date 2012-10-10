package nl.d2n.controller;

import nl.d2n.model.ApplicationException;
import nl.d2n.model.UserKey;
import nl.d2n.service.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("profile")
public class ProfileController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private ProfileService profileService;

    @RequestMapping(method = RequestMethod.POST)
    public void updateMyZone(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "key", required = true) UserKey key
    ) throws IOException, ApplicationException {
        profileService.updateProfile(key);
    }
}
