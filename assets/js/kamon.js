$(document).ready(function () {
	$('label.tree-toggler').click(function () {
		$(this).parent().children('ul.tree').toggle(300);
	});

	// tooltip
  $('[data-toggle="tooltip"]').tooltip();

	$(function(){
	  $('a').each(function() {
	    if ($(this).prop('href') == window.location.href) {
	      $(this).addClass('active');
	    }
	  });
	});

});
