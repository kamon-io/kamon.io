$(document).ready(function () {
	$('label.tree-toggler').click(function () {
		$(this).parent().children('ul.tree').toggle(300);
	});

	// tooltip
  $('[data-toggle="tooltip"]').tooltip();

	$(function(){
	  $('div#doc-tree a').each(function() {
	    if ($(this).prop('href') == window.location.href) {
	      $(this).addClass('active');
				$(this).parent().parent().parent().find('label').addClass('active');
	    }
	  });
	});

});
