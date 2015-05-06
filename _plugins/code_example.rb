#encoding: utf-8
module Jekyll
  class CodeExampleBlock < Liquid::Block

    def initialize(tag_name, text, tokens)
      super
    end

    def render(context)
      id_prefix = Random.rand(1000000).to_s
      serialised_snippets = super.split("\n---\n").drop(1)
      snippets = serialised_snippets.map { |snippet_string| YAML::load(snippet_string) }

      navegation_tabs = '<ul class="nav nav-tabs language-tab" role="tablist">'
      tab_contents = '<div class="tab-content">'

      snippets.each_with_index { |snippet, index|
        active = ''
        content_active = ''

        if(index == 0)
          active = ' class="active"'
          content_active = 'active'
        end

        navegation_tabs = navegation_tabs + '<li role="presentation"' + active + '><a href="#' + id_prefix + '-' + snippet[:language] + '-' + index.to_s + '" role="tab" data-toggle="tab">' + snippet[:label] + '</a></li>'
        tab_contents = tab_contents + '<div role="tabpanel" class="tab-pane '+content_active+'" id="' + id_prefix + '-' + snippet[:language] + '-' + index.to_s + '"><pre class="line-numbers language-'+ snippet[:language] +'"><code class="language-'+ snippet[:language] +'">'+ snippet[:code] +'</code></pre></div>'
      }

      navegation_tabs = navegation_tabs + '</ul>'
      tab_contents = tab_contents + '</div>'

      '<div>' + navegation_tabs + tab_contents + '</div>'
    end
  end

  class LanguageTag < Liquid::Tag

    # The format each language example is:
    #   {% language language_name path/to/file [start:n] [end:n] [label:"text"] %}
    #
    def initialize(tag_name, text, tokens)
      super
      parts = text.split(' ')
      if parts.count < 2
        puts "Can't initialise language tag without at least the language and file path parameters.".yellow
        return "Can't initialise language tag without at least the language and file path parameters".yellow
      end

      @language = parts[0]
      @file = parts[1]

      @tag = text.match(/\btag:([0-9A-Za-z\-\_]*)/)
      if @tag.nil?
        return "Not tag attribute specified"
      end

      @label = text.match(/\blabel:"(.*)"/)
      @label = @label.nil? ? @language.capitalize : @label.captures[0]

    end

    def render(context)
      code_dir = context.registers[:site].config['code_examples_folder'].sub(/^\//,'')
      code_path = (Pathname.new(context.registers[:site].source) + code_dir).expand_path
      file_path = code_path + @file

      if File.symlink?(code_path)
        puts "Code directory '#{code_path}' cannot be a symlink".yellow
        return "Code directory '#{code_path}' cannot be a symlink".yellow
      end

      unless file_path.file?
        puts "File #{file_path}- could not be found".yellow
        return "File #{file_path} could not be found".yellow
      end

      config = {}
      config[:language] = @language
      config[:code] = get_range(file_path.read, @tag)
      config[:label] = @label

      YAML::dump(config)
    end


    def get_range (code, tag)
      lines = code.lines.to_a
      line_count = lines.size
      start_line = code.lines.find_index { |l| l.include? "#{tag}:start" }
      end_line = code.lines.find_index { |l| l.include? "#{tag}:end" }

      raise "#{@file} does not contain the #{tag}:start element." if start_line.nil?
      raise "#{@file} does not contain the #{tag}:end element." if end_line.nil?

      code = lines.slice(start_line + 1, end_line - start_line - 1).join("").strip

      CGI::escapeHTML(code)
    end
  end
end

Liquid::Template.register_tag('language', Jekyll::LanguageTag)
Liquid::Template.register_tag('code_example', Jekyll::CodeExampleBlock)
