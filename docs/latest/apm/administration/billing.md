---
title: 'Kamon APM | Billing | Kamon Documentation'
description: 'Keep track of your billing history and manage your subscription plan and payment details from within Kamon APM'
layout: docs
---

{% include toc.html %}

Billing
=======

{% lightbox /assets/img/apm/billing-page.png %}
Billing
{% endlightbox %}

The billing page offers you insights into your payment history and payment status, details about your [plan], as well as the ability to manage your payment and billing details. For each period you were billed, you can see the billing date, the amount you were billed, and the invoice status (pending or paid). You can also download invoices in English (ENG) or Croatian (HRV). Both documents will have the same pricing breakdown, but the document in Croatian is official, and will have additional information, as required by Croatian tax authorities. The invoices will be downloaded as PDF documents.

{% alert info %}
Billing details are only available if you are on a paid plan, or were on the paid plan at some point in the past. Read about the [plans][plan] for more details.
{% endalert %}

{% lightbox /assets/img/apm/downgrade-dialog.png %}
Downgrade Dialog
{% endlightbox %}

In the sidebar, several contextual actions will be available. On the top, you will be able to see payment breakdown details for your current plan. Note that you can *always* change your plan by upgrading or downgrading. Clicking on the button will open the change plan dialog, where you can see details about each plan and start the upgrade or downgrade flow.

{% lightbox /assets/img/apm/upgrade-billing.png %}
Billing Information
{% endlightbox %}

{% lightbox /assets/img/apm/upgrade-payment-method.png %}
Credit Card Information
{% endlightbox %}

The first time you upgrade from the Developer plan, you will be required to set up your billing information and credit card information. Make sure the billing information is correct, as it will appear in invoices as such! The payment information will be stored using [BrainTree](https://www.braintreepayments.com/) and will be kept secure. We do not store your payment data inside of our own databases.

Subsequent upgrades or plan changes will allow you to use the already specified payment method information, or to change it. Additionally, you can always see the currently set information in the sidebar of the billing page, and change them from there. Any member of the organization will be able to change this information, even if you are not an administrator!

You can access the billing page from the user menu in the bottom left of the screen, or from any other Administration page using the tabs at the top of the screen.

{% lightbox /assets/img/apm/menu-profile.png %}
Menu
{% endlightbox %}

[plan]: /apm/pricing/
