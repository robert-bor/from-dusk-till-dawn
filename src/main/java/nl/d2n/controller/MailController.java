package nl.d2n.controller;

import nl.d2n.model.*;
import nl.d2n.service.MailService;
import nl.d2n.util.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("mail")
public class MailController extends AbstractController {

    @Autowired
    private MailService mailService;

    @RequestMapping(method = RequestMethod.GET)
    public void getInboxAlert(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(value = "key", required = true) UserKey key
            ) throws IOException, ApplicationException, MissingServletRequestParameterException {
        InboxAlert inboxAlert = mailService.getInboxAlert(key);
        writeJsonStringToResponse(GsonUtil.objectToJsonNoEscaping(inboxAlert), response);
    }
}
