$ ->
  $.get routes.controllers.Actors.list().url, (actors) ->
    $.each actors, (index, actor) ->
      $("#actorList").append $("<li>").text actor.firstName + " " + actor.lastName
