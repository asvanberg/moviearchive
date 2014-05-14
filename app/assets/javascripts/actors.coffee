$ ->
  $.get routes.controllers.Actors.listJson().url, (actors) ->
    $.each actors, (index, actor) ->
      $("#actorList").append $("<li>").text actor.firstName + " " + actor.lastName
