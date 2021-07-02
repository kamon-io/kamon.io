---
title: 'Kamon APM | Organization Management | Kamon Documentation'
description: 'Learn about managing your organization members, administration privileges, and customizing your organization in Kamon APM'
layout: docs
---

{% include toc.html %}

Organization Management
=======================

{% lightbox /assets/img/apm/members-page.png %}
Organization Membership
{% endlightbox %}

On the organization member management page, you can manage members invitations and existing organization members. You will only be able to see the current organization here, and will need to use the menu if you wish to manage another organization's members.

{% alert info %}
Multi-user organizations are a premium feature available only on one of the [paid plans].
Organizations on the Developer plan cannot invite other users.
{% endalert %}

{% lightbox /assets/img/apm/members-page-blank.png %}
Name Organization
{% endlightbox %}

When you first create your organization by signing up, it will not have a name. You will need to assign it one to be able to invite users to your organization.

{% lightbox /assets/img/apm/pending-user-list.png %}
Pending Users and Invite Button
{% endlightbox %}

Once you have named your organization, you will be able to invite new users. If there are any pending invitations, a list of them will be visible, listing their e-mail and offering you a context menu. When expanding it, you can re-send an invitation, if case the original invitation e-mail was not sent, or was deleted. If you are an **Administrator**, you will also be able to rescind an invitation. If you do this, the unique token generated for the invite will be invalidated and the user will not be able to join your organization. You will see an Invite user button in the top right (accessible to all users). Upon clicking on it, an Invite User prompt will open, where you will be able to invite new users by entering their e-mail. The user will receive an invitation and, upon signing up or in (if they already have an account), they will automatically be added to your organization in the role of User.

{% lightbox /assets/img/apm/invite-user.png %}
Menu
{% endlightbox %}

The page additionally contains a list of active users, with their name, e-mail address, and current role (User or Admin). If you are an administrator yourself, you will also be able to access a context menu, where you you will be presented with two actions - changing a user's role between Admin and regular User, and deleting the user. Both actions will require confirmation. You will also be able to demote yourself to a regular user, as long as there is at least one other administrator present in the organization.

{% lightbox /assets/img/apm/active-user-list.png %}
Active User List
{% endlightbox %}

You can access the organization management page from the user menu in the bottom left of the screen, or from any other Administration page using the tabs at the top of the screen.

{% lightbox /assets/img/apm/menu-profile.png %}
Menu
{% endlightbox %}

[paid plans]: /apm/pricing/
