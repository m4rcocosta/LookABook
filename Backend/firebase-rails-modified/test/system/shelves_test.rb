require "application_system_test_case"

class ShelvesTest < ApplicationSystemTestCase
  setup do
    @shelf = shelves(:one)
  end

  test "visiting the index" do
    visit shelves_url
    assert_selector "h1", text: "Shelves"
  end

  test "creating a Shelf" do
    visit shelves_url
    click_on "New Shelf"

    fill_in "Name", with: @shelf.name
    click_on "Create Shelf"

    assert_text "Shelf was successfully created"
    click_on "Back"
  end

  test "updating a Shelf" do
    visit shelves_url
    click_on "Edit", match: :first

    fill_in "Name", with: @shelf.name
    click_on "Update Shelf"

    assert_text "Shelf was successfully updated"
    click_on "Back"
  end

  test "destroying a Shelf" do
    visit shelves_url
    page.accept_confirm do
      click_on "Destroy", match: :first
    end

    assert_text "Shelf was successfully destroyed"
  end
end
