package server.controllers;

import server.Logger;

import server.models.Prediction;
import server.models.services.PredictionService;

import server.models.User;
import server.models.services.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;

@Path("predict/")
public class PredictionController {

    @POST
    @Path("make")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)

    public String make(@CookieParam("sessionToken") Cookie sessionToken,
                       @CookieParam("choiceID") Cookie choiceId,
                       @CookieParam("questionNum") Cookie questionNum) {

        Logger.log("Makes a prediction.");

        int userId = getUserId(sessionToken.getValue());

        return PredictionService.insert(new Prediction(Prediction.nextId(), userId, questionNum.getValue(), choiceId.getValue()));
    }

    private static int getUserId(String sessionToken) {
        UserService.selectAllInto(User.users);

        for (User u : User.users) {
            if (u.getSessionToken().equals(sessionToken)) {
                return u.getUserID();
            }
        }

        return -1;
    }
}