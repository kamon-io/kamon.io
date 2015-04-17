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
				//$(this).parent().closest('li').children('label').addClass('active');
				$(this).parent().parent().parent().find('label').addClass('active');
				//$(this).parent().parent().parent().parent().parent().find('label').addClass('active');
	    }
	  });
	});

});
