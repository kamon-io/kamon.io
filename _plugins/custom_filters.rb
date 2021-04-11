module Jekyll
  module CustomFilters

    def sidebar_link(current_page, classes, expected_page)
      if current_page.eql? expected_page
        'class="' + classes + ' current-page" href="' + expected_page + '"'
      else
        'class="' + classes + '" href="' + expected_page + '"'
      end
    end

    def bintray_latest_release(module_name)
      '<a href="https://bintray.com/kamon-io/releases/'+ module_name +'/_latestVersion">' +
        '<img src="https://api.bintray.com/packages/kamon-io/releases/'+ module_name +'/images/download.svg" alt="Download">' +
      '</a>'
    end

    def maven_latest_release(module_name)
      '<img src="https://maven-badges.herokuapp.com/maven-central/io.kamon/'+ module_name +'_2.12/badge.svg" alt="Download">'
    end

    def contains(list, element)
      !list.nil? && list.include?(element)
    end

    def drop(list, count)
      !list.nil? && list.drop(count)
    end

    def match(variable, left, right)
      if variable
        left
      else
        right
      end
    end

    def start_with(text, query)
      return !text.nil? && !query.nil? && ((text.start_with? query) || (text.equal? query))
    end

    def if_truthy(variable, left, right)
      if variable
        left
      else
        right
      end
    end

  end
end

Liquid::Template.register_filter(Jekyll::CustomFilters)