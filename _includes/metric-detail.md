{% assign path_sections = page.url | split: '/' %}
{% assign current_version = path_sections[2] %}
{% assign metric_info = site.data.docs[current_version].metrics[include.name] %}

<div class="container-fluid">
  <div class="row metric-detail elevated-container py-3 px-2 my-2" markdown="0" data-toggle="collapse" data-target="#{{ include.name | replace: ".", "_" }}" style="cursor: pointer;">
    <div class="col-12 w-100">
    <a class="no-decoration">
      <strong class="metric-name mr-2 w-100">{{ include.name}}</strong> 
      <span style="display: inherit;">{{ metric_info.instrument_type }}</span>
      <div class="metric-description">{{ metric_info.description }}</div>
    </a>
    </div>
    <div id="{{ include.name | replace: ".", "_" }}" class="col-12 collapse metric-tags">
      <div class=""></div>
      {% if metric_info.tags %}
        <br>
        <div class="mb-1">Tags included by default:</div>
        <ul class="pl-4">
          {% for tag in metric_info.tags %}
            <li><span class="metric-tag-name text-dark-2">{{ tag.name }}</span>: {{ tag.description }}</li>
          {% endfor %}
        </ul>
      {% endif %}
    </div>
  </div>
</div>